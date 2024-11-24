package com.example.demo.dto.user

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

import java.time.LocalDate

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UserCreateRequestDTO(
    val name: String,
    val birthDate: LocalDate,
    val password: String,
    val phoneNumber: String
)