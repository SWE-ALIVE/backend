package com.example.demo.dto

import java.util.*

data class DeviceUsageRequestDTO(
    val userId: UUID,
    val deviceId: UUID
)