package com.example.demo.dto

import java.time.LocalDate
import java.util.*

data class UserDTO(
    val id: UUID,
    val name: String,
    val birthDate: LocalDate,
    val phoneNumber: String
)
