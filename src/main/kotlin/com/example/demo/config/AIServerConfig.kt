package com.example.demo.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class AIServerConfig(
    @Value("\${ai.server.url}") val url: String
)
