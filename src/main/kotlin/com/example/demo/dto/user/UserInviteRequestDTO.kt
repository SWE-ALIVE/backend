package com.example.demo.dto.user

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UserInviteRequestDTO (
    val channelId: String,
    val deviceIds: List<String>
)