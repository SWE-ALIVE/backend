package com.example.demo.dto.sendbird

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SendbirdUserInviteRequest(
    val channelUrl: String,
    val userIds: List<String>
)
