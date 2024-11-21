package com.example.demo.dto

import com.example.demo.model.DeviceCategory
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class DeviceCreateRequestDTO(
    @JsonProperty("product_number")
    val productNumber: String,

    val category: DeviceCategory,

    @JsonProperty("extra_function")
    val extraFunction: String,
)