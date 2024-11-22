package com.example.demo.dto.channel

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreateChannelRequest(
    val name: String,
    val deviceIds: List<String>,
    val operatorIds: List<String>
)
