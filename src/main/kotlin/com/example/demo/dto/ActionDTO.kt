package com.example.demo.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ActionDTO(
    val actionDescription: String?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)