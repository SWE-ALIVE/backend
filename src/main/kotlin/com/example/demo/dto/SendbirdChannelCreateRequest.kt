package com.example.demo.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SendbirdChannelCreateRequest(
    val name: String,
    val channelUrl: String,
    val userIds: List<String>,
    val operatorIds: List<String>
)
