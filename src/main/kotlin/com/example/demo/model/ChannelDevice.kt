package com.example.demo.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonProperty

import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    name = "channel_device",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["channel_id", "device_id"])  // channel_id와 device_id의 유니크 제약 추가
    ]
)
data class ChannelDevice(
    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "channel_id", nullable = false)
    @JsonBackReference
    val channel: Channel,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "device_id", nullable = false)
    @JsonBackReference
    val device: Device,

    @JsonProperty("device_status")
    var deviceStatus: Boolean
)