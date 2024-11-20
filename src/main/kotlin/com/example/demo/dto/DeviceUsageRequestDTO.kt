package com.example.demo.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class DeviceUsageRequestDTO(
    @JsonProperty("user_id") val userId: UUID,   // user_id -> userId
    @JsonProperty("device_id") val deviceId: UUID // device_id -> deviceId
)