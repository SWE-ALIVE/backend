package com.example.demo.dto

import java.time.LocalDate
import java.time.LocalTime

data class UserResponseDTO(
    val id: String,
    val name: String,
    val birthDate: LocalDate,
    val phoneNumber: String
)
