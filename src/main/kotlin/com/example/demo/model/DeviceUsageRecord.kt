package com.example.demo.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonProperty

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "device_usage_record")
data class DeviceUsageRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_device_id", nullable = false)
    @JsonBackReference
    val userDevice: UserDevice,

    @Column(nullable = false)
    @JsonProperty("usage_date")
    val usageDate: LocalDate,

    @Column(nullable = false)
    @JsonProperty("action_description")
    val actionDescription: String,

    @Column
    @JsonProperty("start_time")
    val startTime: LocalDateTime,

    @Column
    @JsonProperty("end_time")
    val endTime: LocalDateTime
)