package com.example.demo.dto.channel

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SendMessageRequest(
    val channelUrl: String,
    val userId: String,
    val message: String
)
