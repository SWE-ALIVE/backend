package com.example.demo.dto

import java.util.UUID

data class UserDeviceDTO (
    val category: String,
    val deviceId: UUID,
    val deviceName: String
)