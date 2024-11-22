package com.example.demo.dto.channel

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ChannelDeviceDTO(
    val id: String,
    val name: String,
    val category: String?,
    var deviceStatus: Boolean? = true
)




