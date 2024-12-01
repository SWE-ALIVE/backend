package com.example.demo.service

import com.example.demo.dto.user.CreateUserResponse
import com.example.demo.util.ApiHelper
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class DialogUserService(
    private val restTemplate: RestTemplate,
    private val apiHelper: ApiHelper
) {
    fun getUsers(): ResponseEntity<String> {
        val url = apiHelper.buildUrl("users")
        val request = HttpEntity<Any>(apiHelper.getHeaders())
        return restTemplate.exchange(url, HttpMethod.GET, request, String::class.java)
    }

    fun createUser(userId: String, nickname: String, profileUrl: String): ResponseEntity<CreateUserResponse> {
        val url = apiHelper.buildUrl("users")
        val body = mapOf(
            "user_id" to userId,
            "nickname" to nickname,
            "profile_url" to profileUrl
        )
        val request = HttpEntity(body, apiHelper.getHeaders())
        return restTemplate.postForEntity(url, request, CreateUserResponse::class.java)
    }

    fun deleteUser(userId: String): ResponseEntity<String> {
        val url = apiHelper.buildUrl("users/$userId")
        val request = HttpEntity<Any>(apiHelper.getHeaders())
        return restTemplate.exchange(url, HttpMethod.DELETE, request, String::class.java)
    }
}
