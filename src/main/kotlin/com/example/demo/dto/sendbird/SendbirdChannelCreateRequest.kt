package com.example.demo.dto.sendbird

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SendbirdChannelCreateRequest(
    val name: String,
    val channelId: String,
    val deviceIds: List<String>,
    val operatorIds: List<String>
)
