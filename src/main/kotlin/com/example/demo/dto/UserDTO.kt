package com.example.demo.dto

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class UserDTO(
    val id: UUID,
    val name: String,
    val birthDate: LocalDate,
    val phoneNumber: String
)
