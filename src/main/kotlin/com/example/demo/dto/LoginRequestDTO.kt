package com.example.demo.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginRequestDTO(
    @JsonProperty("phone_number") val phoneNumber: String,  // phone_number -> phoneNumber
    @JsonProperty("password") val password: String
)
