package com.example.demo.dto.sendbird

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SendbirdUserCreateRequest(
    val userId: String,
    val nickname: String,
    val profileUrl: String
)
