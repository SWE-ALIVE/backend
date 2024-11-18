package com.example.demo.dto.sendbird

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SendbirdUserCreateResponse(
    val userId: String,
    val nickname: String,
    val profileUrl: String,
    val isCreated: String
)
