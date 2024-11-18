package com.example.demo.dto

import java.util.*

data class DeviceStatusRequestDTO(
    val chatroomId: UUID,  // 채팅방 ID
    val deviceId: UUID,    // 장치 ID
    val deviceStatus: Boolean
)