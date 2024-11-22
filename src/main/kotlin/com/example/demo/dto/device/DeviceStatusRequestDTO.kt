package com.example.demo.dto.device

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class DeviceStatusRequestDTO(
    val channelId: UUID,
    val deviceId: UUID,
    val deviceStatus: Boolean
)