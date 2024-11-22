package com.example.demo.dto.user

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDate
import java.util.UUID


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UserResponseDTO(
    val id: UUID,
    val name: String,
    val birthDate: LocalDate,
    val phoneNumber: String
)
