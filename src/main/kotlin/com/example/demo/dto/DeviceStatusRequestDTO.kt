package com.example.demo.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class DeviceStatusRequestDTO(
    @JsonProperty("chatroom_id") val chatroomId: UUID,  // chatroom_id -> chatroomId
    @JsonProperty("device_id") val deviceId: UUID,      // device_id -> deviceId
    @JsonProperty("device_status") val deviceStatus: Boolean  // device_status -> deviceStatus
)