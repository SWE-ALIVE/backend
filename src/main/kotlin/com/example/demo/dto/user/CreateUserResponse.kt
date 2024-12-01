package com.example.demo.dto.user

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreateUserResponse(
    val userId: String,
    val nickname: String,
    val profileUrl: String,
    val isCreated: String
)
