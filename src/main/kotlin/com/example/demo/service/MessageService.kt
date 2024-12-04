package com.example.demo.service

import com.example.demo.config.AIServerConfig
import com.example.demo.dto.ai.AIMessageRequest
import com.example.demo.dto.ai.AIMessageResponse
import com.example.demo.model.DeviceCategory
import com.example.demo.model.Device
import com.example.demo.repository.DeviceRepository
import com.example.demo.util.ApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class MessageService(
    private val restTemplate: RestTemplate,
    private val apiHelper: ApiHelper,
    private val aiServerConfig: AIServerConfig,
    private val deviceRepository: DeviceRepository
) {
    private val logger = LoggerFactory.getLogger(MessageService::class.java)

    /**
     * Send a message to a Sendbird channel.
     */
    fun sendMessagesToChannel(channelUrl: String, userId: String, message: String): ResponseEntity<String> {
        val url = apiHelper.buildUrl("group_channels/$channelUrl/messages")
        val body = mapOf(
            "message_type" to "MESG",
            "user_id" to userId,
            "message" to message
        )
        val request = HttpEntity(body, apiHelper.getHeaders())
        return restTemplate.postForEntity(url, request, String::class.java)
    }

    /**
     * Query messages from a Sendbird channel.
     */
    fun queryMessagesFromChannel(channelUrl: String, messageTs: String, limit: Int?): ResponseEntity<String> {
        val validMessageTs = messageTs.ifBlank { System.currentTimeMillis().toString() }

        val urlBuilder = UriComponentsBuilder.fromHttpUrl(apiHelper.buildUrl("group_channels/$channelUrl/messages"))
            .queryParam("message_ts", validMessageTs)

        if (limit != null && limit > 0) {
            urlBuilder.queryParam("prev_limit", limit)
        }

        val url = urlBuilder.toUriString()
        val request = HttpEntity<Any>(apiHelper.getHeaders())
        return restTemplate.exchange(url, HttpMethod.GET, request, String::class.java)
    }

    /**
     * Process AI messages by sending them to the AI server and then dispatching to Sendbird channels.
     */
    suspend fun processAIMessage(request: AIMessageRequest): AIMessageResponse = coroutineScope {
        // 1. AI 서버에 요청 전송
        val aiResponse = sendToAIServer(request)

        // 2. 샌드버드를 통해 메시지 전송 (최적화된 방식)
        sendMessages(aiResponse, request)

        // 3. 응답 반환
        AIMessageResponse(
            messages = aiResponse.messages,
            context = aiResponse.context
        )
    }

    /**
     * Send a request to the AI server and retrieve the response.
     */
    private fun sendToAIServer(request: AIMessageRequest): AIMessageResponse {
        // 1.1 AI 서버 URL 불러오기
        val url = aiServerConfig.url
        return try {
            // 1.2 AI 서버에 요청 전송 및 응답 처리
            val response: ResponseEntity<AIMessageResponse> = restTemplate.postForEntity(url, request, AIMessageResponse::class.java)
            if (response.statusCode.is2xxSuccessful && response.body != null) {
                response.body!!
            } else {
                logger.error("AI 서버 응답 오류: ${response.statusCode} - ${response.body}")
                throw RuntimeException("AI 서버로부터 유효한 응답을 받지 못했습니다.")
            }
        } catch (e: Exception) {
            logger.error("AI 서버 통신 중 오류 발생", e)
            throw e
        }
    }

    /**
     * Send messages to Sendbird channels using optimized querying and asynchronous processing.
     */
    private suspend fun sendMessages(aiResponse: AIMessageResponse, request: AIMessageRequest) = coroutineScope {
        // 2.1. 고유한 도메인 추출
        val uniqueDomains = aiResponse.messages.map { it.domain }.distinct()

        // 2.2. 도메인별 장치 조회 (배치 쿼리)
        val categories = uniqueDomains.mapNotNull { domain ->
            try {
                DeviceCategory.valueOf(domain)
            } catch (e: IllegalArgumentException) {
                logger.error("Invalid domain encountered: $domain", e)
                null
            }
        }

        // 한 번의 쿼리로 모든 필요한 장치 조회
        val devices: List<Device> = deviceRepository.findByCategoryIn(categories)

        // 도메인과 장치를 매핑
        val categoryDeviceMap: Map<DeviceCategory, Device> = devices.associateBy { it.category }

        // 2.3. 메시지 전송 작업 생성 및 비동기로 실행
        val sendJobs = aiResponse.messages.map { message ->
            async(Dispatchers.IO) {
                try {
                    val category = DeviceCategory.valueOf(message.domain)
                    val device = categoryDeviceMap[category]
                        ?: throw IllegalArgumentException("Device not found for category: ${message.domain}")

                    val response = sendMessagesToChannel(
                        channelUrl = request.channelUrl,
                        userId = device.id.toString(),
                        message = message.message
                    )

                    if (!response.statusCode.is2xxSuccessful) {
                        logger.error("Failed to send message to Sendbird. Status: ${response.statusCode}, Body: ${response.body}")
                    } else {
                        logger.info("Message sent successfully to channel: ${request.channelUrl} by user: ${device.id}")
                    }
                } catch (e: Exception) {
                    logger.error("Error sending message for domain: ${message.domain}", e)
                }
            }
        }

        // 2.4. 모든 메시지 전송 작업 완료 대기
        sendJobs.awaitAll()
    }
}
