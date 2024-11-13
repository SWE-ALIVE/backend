package com.example.demo.Model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "device")
data class Device(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(name = "product_number", nullable = false)
    val productNumber: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val category: DeviceCategory,

    @Column(name = "extra_function")
    val extraFunction: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    // 나의 채팅방 목록만 뽑아 올 수 있음.
    @OneToMany(mappedBy = "device", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val chatRoomDevices: List<ChatRoomDevice> = mutableListOf()
)
