package com.example.demo.dto

import java.time.LocalDate

data class UserResponseDTO(
    val id: String,
    val name: String,
    val birthDate: LocalDate,
    val phoneNumber: String
)
