package com.example.demo.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class DialogConfig(
    @Value("\${sendbird.api-key}") val apiKey: String,
    @Value("\${sendbird.app-id}") val appId: String
)
