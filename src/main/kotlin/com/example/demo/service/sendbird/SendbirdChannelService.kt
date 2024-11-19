package com.example.demo.service.sendbird

import com.example.demo.dto.sendbird.SendbirdChannelCreateRequest
import com.example.demo.util.SendbirdApiHelper
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SendbirdChannelService(
    private val restTemplate: RestTemplate,
    private val apiHelper: SendbirdApiHelper
) {
    fun getGroupChannels(): ResponseEntity<String> {
        val url = apiHelper.buildUrl("group_channels")
        val request = HttpEntity<Any>(apiHelper.getHeaders())
        return restTemplate.exchange(url, HttpMethod.GET, request, String::class.java)
    }

    fun createGroupChannel(request: SendbirdChannelCreateRequest): ResponseEntity<String> {
        val url = apiHelper.buildUrl("group_channels")
        val body = mapOf(
            "name" to request.name,
            "channel_url" to request.channelUrl,
            "user_ids" to request.userIds,
            "operator_ids" to request.operatorIds
        )
        val entity = HttpEntity(body, apiHelper.getHeaders())
        return restTemplate.postForEntity(url, entity, String::class.java)
    }

    fun addUserToChannel(channelUrl: String, userIds: List<String>): ResponseEntity<String> {
        val url = apiHelper.buildUrl("group_channels/$channelUrl/invite")
        val body = mapOf("user_ids" to userIds)
        val request = HttpEntity(body, apiHelper.getHeaders())
        return restTemplate.postForEntity(url, request, String::class.java)
    }

    fun getUsersInChannel(channelUrl: String): ResponseEntity<String> {
        val url = apiHelper.buildUrl("group_channels/$channelUrl/members")
        val request = HttpEntity<Any>(apiHelper.getHeaders())
        return restTemplate.exchange(url, HttpMethod.GET, request, String::class.java)
    }
}
