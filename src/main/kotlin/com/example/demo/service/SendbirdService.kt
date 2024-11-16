package com.example.demo.service

import com.example.demo.config.SendbirdConfig
import com.example.demo.dto.SendbirdChannelCreateRequest
import com.example.demo.dto.SendbirdUserCreateResponse
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
    private val headers = HttpHeaders().apply {
        add("Api-Token", sendbirdConfig.apiKey)
        contentType = MediaType.APPLICATION_JSON
    }

    fun getUsers(): ResponseEntity<String> {
        val url = UriComponentsBuilder
            .fromHttpUrl("$baseUrl/users")
            .toUriString()

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
    fun createUser(userId: String, nickname: String, profileUrl: String): ResponseEntity<SendbirdUserCreateResponse> {
        val url = "$baseUrl/users"

        val body = mapOf("user_id" to userId, "nickname" to nickname, "profile_url" to profileUrl)
        val request = HttpEntity(body, headers)

        return restTemplate.postForEntity(url, request, SendbirdUserCreateResponse::class.java)
    }

    fun getGroupChannels(): ResponseEntity<String> {
        val url = "$baseUrl/group_channels"

        // HttpEntity 객체에 헤더만 포함
        val requestEntity = HttpEntity<Any>(headers)

        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, String::class.java)
    }


    fun createGroupChannel(sendbirdChannelCreateRequest: SendbirdChannelCreateRequest): ResponseEntity<String> {
        val url = "$baseUrl/group_channels"
        val body = mapOf(
            "name" to sendbirdChannelCreateRequest.name,
            "channel_url" to sendbirdChannelCreateRequest.channelUrl,
            "user_ids" to sendbirdChannelCreateRequest.userIds,
            "operator_ids" to sendbirdChannelCreateRequest.operatorIds
        )
        val request = HttpEntity(body, headers)
        return restTemplate.postForEntity(url, request, String::class.java)
    }

    fun addUserToChannel(channelUrl: String, userIds: List<String>): ResponseEntity<String> {
        val url = "$baseUrl/group_channels/$channelUrl/invite"
        val body = mapOf("user_ids" to userIds)
        val request = HttpEntity(body, headers)
        return restTemplate.postForEntity(url, request, String::class.java)
    }

    fun sendMessagesToChannel(channelUrl: String, userId: String, message: String): ResponseEntity<String> {
        val url = "$baseUrl/group_channels/$channelUrl/messages"
        val body = mapOf(
            "message_type" to "MESG",
            "user_id" to userId,
            "message" to message
        )
        val request = HttpEntity(body, headers)
        return restTemplate.postForEntity(url, request, String::class.java)
    }

    fun queryMessagesFromChannel(channelUrl: String, messageTs: String, limit: Int): ResponseEntity<String> {
        val validMessageTs = messageTs.ifEmpty { System.currentTimeMillis().toString() }

        val url = UriComponentsBuilder.fromHttpUrl("$baseUrl/group_channels/$channelUrl/messages")
            .queryParam("message_ts", validMessageTs)
            .queryParam("prev_limit", limit)
            .toUriString()

        val request = HttpEntity<Any>(headers)
        return restTemplate.exchange(url, HttpMethod.GET, request, String::class.java)
    }

}
