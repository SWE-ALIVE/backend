package com.example.demo.dto.channel

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ChannelDeviceDTO(
    val id: String,
    val category: String?,
    var name: String,
    val nickname: String,
    var deviceStatus: Boolean? = true
)




