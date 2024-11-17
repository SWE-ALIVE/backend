package com.example.demo.dto

import java.time.LocalDate
import java.time.LocalTime

data class DeviceUsageResponseDTO(
    val deviceName: String,
    val chatRooms: List<ChatRoomDTO>,
    val actions: List<ActionDTO>
)

data class ChatRoomDTO(
    val chatRoomName: String,
    val chatRoomDevices: List<String> // 기기 이름의 리스트
)

data class ActionDTO(
    val actionDescription: String?,
    val usageDate: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
)
