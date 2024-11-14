package com.example.demo

import com.example.demo.config.SendbirdConfig
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class SendbirdService(
    private val restTemplate: RestTemplate,
    private val sendbirdConfig: SendbirdConfig
) {
    private val baseUrl = "https://api-${sendbirdConfig.appId}.sendbird.com/v3"
    private val logger = LoggerFactory.getLogger(SendbirdService::class.java)


    fun getUsers(): ResponseEntity<String> {
        val url = UriComponentsBuilder
            .fromHttpUrl("$baseUrl/users")
            .toUriString()

        val headers = HttpHeaders().apply {
            set("Api-Token", sendbirdConfig.apiKey)
            contentType = MediaType.APPLICATION_JSON
        }

        logger.debug("Request URL: $url")
        logger.debug("Request Headers: $headers")

        val entity = HttpEntity<String>(headers)

        return try {
            val response = restTemplate.exchange(url, HttpMethod.GET, entity, String::class.java)
            logger.debug("Response: ${response.body}")
            response
        } catch (ex: Exception) {
            logger.error("Error during API call", ex)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("API call failed: ${ex.message}")
        }
    }
    // 유저 생성
    fun createUser(userId: String, nickname: String): ResponseEntity<String> {
        val url = "$baseUrl/users"
        val headers = HttpHeaders().apply {
            add("Api-Token", sendbirdConfig.apiKey)
            contentType = MediaType.APPLICATION_JSON
        }
        val body = mapOf("user_id" to userId, "nickname" to nickname)
        val request = HttpEntity(body, headers)
        return restTemplate.postForEntity(url, request, String::class.java)
    }

    // 메시지 전송
    fun sendMessage(channelUrl: String, userId: String, message: String): ResponseEntity<String> {
        val url = "$baseUrl/group_channels/$channelUrl/messages"
        val headers = HttpHeaders().apply {
            add("Api-Token", sendbirdConfig.apiKey)
            contentType = MediaType.APPLICATION_JSON
        }
        val body = mapOf(
            "message_type" to "MESG",
            "user_id" to userId,
            "message" to message
        )
        val request = HttpEntity(body, headers)
        return restTemplate.postForEntity(url, request, String::class.java)
    }

    fun getOpenChannels(): List<String>? {
        val appId = sendbirdConfig.appId
        val apiKey = sendbirdConfig.apiKey

        // Sendbird API URL 생성
        val url = UriComponentsBuilder
            .fromHttpUrl("https://api-$appId.sendbird.com/v3/open_channels")
            .toUriString()

        // 헤더 설정
        val headers = HttpHeaders().apply {
            set("Api-Token", apiKey)
        }
        val entity = HttpEntity<String>(headers)

        // API 요청 보내기
        val response = restTemplate.exchange(url, HttpMethod.GET, entity, Map::class.java)

        // 응답 처리
        return if (response.statusCode.is2xxSuccessful) {
            val channels = response.body?.get("channels") as? List<Map<String, Any>>
            channels?.map { it["channel_url"].toString() }
        } else {
            throw RuntimeException("Failed to fetch open channels from Sendbird")
        }
    }
}
