package com.example.demo.dto.channel

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)data class ChannelDTO(
    val channelName: String,
    val channelId : String,
    val channelDevices: List<String>
)