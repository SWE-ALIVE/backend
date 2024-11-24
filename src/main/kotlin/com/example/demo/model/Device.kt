package com.example.demo.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonProperty

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "device")
data class Device(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @Column(nullable = false)
    @JsonProperty("device_name")
    val name: String,

    @Column(nullable = false)
    val nickname: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val category: DeviceCategory,

    @Column(nullable = false)
    @JsonProperty("extra_function")
    val extraFunction: String,

    @Column(nullable = false)
    @JsonProperty("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "device", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    val userDevices: MutableList<UserDevice> = mutableListOf(),

    @OneToMany(mappedBy = "device", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    val channelDevices: MutableList<ChannelDevice> = mutableListOf()
)