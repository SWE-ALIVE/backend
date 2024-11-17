package com.example.demo.dto

import java.time.LocalTime

data class UserResponseDTO(
    val id: String,
    val name: String,
    val birthDate: LocalTime,
    val phoneNumber: String
)
