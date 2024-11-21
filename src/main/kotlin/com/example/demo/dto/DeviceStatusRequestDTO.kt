package com.example.demo.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class DeviceStatusRequestDTO(
    @JsonProperty("channel_id") val channelId: UUID,  // channel_id -> channelId
    @JsonProperty("device_id") val deviceId: UUID,      // device_id -> deviceId
    @JsonProperty("device_status") val deviceStatus: Boolean  // device_status -> deviceStatus
)