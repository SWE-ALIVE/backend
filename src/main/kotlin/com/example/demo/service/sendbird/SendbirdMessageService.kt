package com.example.demo.service.sendbird

import com.example.demo.util.SendbirdApiHelper
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class SendbirdMessageService(
    private val restTemplate: RestTemplate,
    private val apiHelper: SendbirdApiHelper
) {
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

        if (limit != null) {
            urlBuilder.queryParam("prev_limit", limit)
        }
        val url = urlBuilder.toUriString()

        val request = HttpEntity<Any>(apiHelper.getHeaders())
        return restTemplate.exchange(url, HttpMethod.GET, request, String::class.java)
    }
}
