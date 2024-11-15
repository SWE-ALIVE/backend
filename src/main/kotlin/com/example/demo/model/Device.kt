package com.example.demo.model

import jakarta.persistence.*
import main.kotlin.com.example.demo.model.UserDevice
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "device")
data class Device(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @Column(nullable = false)
    val productNumber: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val category: DeviceCategory,

    @Column(nullable = false)
    val extraFunction: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "device", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val userDevices: MutableList<UserDevice> = mutableListOf(),

    @OneToMany(mappedBy = "device", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val chatRoomDevices: MutableList<ChatRoomDevice> = mutableListOf()
)