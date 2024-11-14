package com.example.demo.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "chatRoomDevice")
data class ChatRoomDevice(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", referencedColumnName = "id", nullable = false)
    val chatRoom: ChatRoom,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", referencedColumnName = "id", nullable = false)
    val device: Device,

    @Column(name = "device_status", nullable = false)
    val deviceStatus: Boolean
)
