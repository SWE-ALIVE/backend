package com.example.demo.dto.device

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class DeviceUsageCreateDTO (
    val  userId: String,
    val  deviceId: String,
    val  actionDescription: String
)