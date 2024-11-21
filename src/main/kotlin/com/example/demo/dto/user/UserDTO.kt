package com.example.demo.dto.user

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.util.UUID

data class UserDTO(
    val id: UUID,

    val name: String,

    @JsonProperty("birth_date")
    val birthDate: LocalDate,

    @JsonProperty("phone_number")
    val phoneNumber: String
)
