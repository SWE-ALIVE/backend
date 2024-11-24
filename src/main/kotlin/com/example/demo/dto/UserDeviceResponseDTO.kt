package com.example.demo.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UserDeviceResponseDTO (
    val category: String,
    val deviceId: UUID,
    val name: String,
    val nickname: String
)