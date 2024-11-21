package com.example.demo.dto

import java.time.LocalDate
import java.time.LocalTime

data class ActionDTO(
    val actionDescription: String?,
    val usageDate: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
)