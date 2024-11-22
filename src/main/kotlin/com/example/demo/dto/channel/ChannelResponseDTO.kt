package com.example.demo.dto.channel

import com.example.demo.model.Channel
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ChannelResponseDTO(
    val channelId: String,
    val channelName: String,
    val devices: List<String>
)
