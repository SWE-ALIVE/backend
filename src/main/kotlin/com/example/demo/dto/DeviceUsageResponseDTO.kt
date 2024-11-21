package com.example.demo.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class DeviceUsageResponseDTO(
    val deviceName: String,
    val channels: List<ChannelDTO>,
    val actions: List<ActionDTO>
)