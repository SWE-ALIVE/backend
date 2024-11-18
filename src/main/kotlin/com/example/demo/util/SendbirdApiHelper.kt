package com.example.demo.util

import com.example.demo.config.SendbirdConfig
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
class SendbirdApiHelper(private val sendbirdConfig: SendbirdConfig) {
    fun getHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            add("Api-Token", sendbirdConfig.apiKey)
            contentType = MediaType.APPLICATION_JSON
        }
    }

    fun buildUrl(endpoint: String): String {
        return "https://api-${sendbirdConfig.appId}.sendbird.com/v3/$endpoint"
    }
}
