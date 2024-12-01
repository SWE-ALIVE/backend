package com.example.demo.service

import com.example.demo.config.AIServerConfig
import com.example.demo.dto.sendbird.AIMessageRequest
import com.example.demo.dto.sendbird.AIMessageResponse
import com.example.demo.util.SendbirdApiHelper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class MessageService(
    private val restTemplate: RestTemplate,
    private val apiHelper: SendbirdApiHelper,
    private val aiServerConfig: AIServerConfig
) {
    private val logger = LoggerFactory.getLogger(MessageService::class.java)

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

    fun queryMessagesFromChannel(channelUrl: String, messageTs: String, limit: Int?): ResponseEntity<String> {
        val validMessageTs = messageTs.ifEmpty { System.currentTimeMillis().toString() }

        val urlBuilder = UriComponentsBuilder.fromHttpUrl(apiHelper.buildUrl("group_channels/$channelUrl/messages"))
            .queryParam("message_ts", validMessageTs)

        if (limit != 0) {
            urlBuilder.queryParam("prev_limit", limit)
        }
        val url = urlBuilder.toUriString()

        val request = HttpEntity<Any>(apiHelper.getHeaders())
        return restTemplate.exchange(url, HttpMethod.GET, request, String::class.java)
    }

    fun processAIMessage(request: AIMessageRequest): AIMessageResponse {
        // 1. AI 서버에 요청 전송
        val aiResponse = sendToAIServer(request)

        // 2. 샌드버드를 통해 메시지 전송
        sendMessagesToChannel(request.channelUrl, request.userId, aiResponse.message)

        // 3. 응답 반환
        return AIMessageResponse(
            sql = aiResponse.sql,
            message = aiResponse.message,
            context = aiResponse.context
        )
    }

    private fun sendToAIServer(request: AIMessageRequest): AIMessageResponse {
        val url = aiServerConfig.url
        return try {
            val response = restTemplate.postForEntity(url, request, AIMessageResponse::class.java)
            if (response.statusCode.is2xxSuccessful && response.body != null) {
                response.body!!
            } else {
                logger.error("AI 서버 응답 오류: ${response.statusCode}")
                throw RuntimeException("AI 서버로부터 유효한 응답을 받지 못했습니다.")
            }
        } catch (e: Exception) {
            logger.error("AI 서버 통신 중 오류 발생", e)
            throw e
        }
    }
}
