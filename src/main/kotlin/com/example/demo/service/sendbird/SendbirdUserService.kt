package com.example.demo.service.sendbird

import com.example.demo.dto.sendbird.SendbirdUserCreateResponse
import com.example.demo.util.SendbirdApiHelper
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SendbirdUserService(
    private val restTemplate: RestTemplate,
    private val apiHelper: SendbirdApiHelper
) {
    fun getUsers(): ResponseEntity<String> {
        val url = apiHelper.buildUrl("users")
        val request = HttpEntity<Any>(apiHelper.getHeaders())
        return restTemplate.exchange(url, HttpMethod.GET, request, String::class.java)
    }

    fun createUser(userId: String, nickname: String, profileUrl: String): ResponseEntity<SendbirdUserCreateResponse> {
        val url = apiHelper.buildUrl("users")
        val body = mapOf(
            "user_id" to userId,
            "nickname" to nickname,
            "profile_url" to profileUrl
        )
        val request = HttpEntity(body, apiHelper.getHeaders())
        return restTemplate.postForEntity(url, request, SendbirdUserCreateResponse::class.java)
    }

    fun deleteUser(userId: String): ResponseEntity<String> {
        val url = apiHelper.buildUrl("users/$userId")
        val request = HttpEntity<Any>(apiHelper.getHeaders())
        return restTemplate.exchange(url, HttpMethod.DELETE, request, String::class.java)
    }
}
