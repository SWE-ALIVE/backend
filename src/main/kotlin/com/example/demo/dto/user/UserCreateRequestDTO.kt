package com.example.demo.dto.user

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class UserCreateRequestDTO(

    val name: String,

    @JsonProperty("birth_date")
    val birthDate: LocalDate,

    val password: String,

    @JsonProperty("phone_number")
    val phoneNumber: String
)