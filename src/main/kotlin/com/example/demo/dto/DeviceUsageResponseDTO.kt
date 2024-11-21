package com.example.demo.dto

data class DeviceUsageResponseDTO(
    val deviceName: String,
    val channels: List<ChannelDTO>,
    val actions: List<ActionDTO>
)