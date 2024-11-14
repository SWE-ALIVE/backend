package com.example.demo.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "chat_room_device")
data class ChatRoomDevice(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "chatroom_id", nullable = false)
    val chatRoom: ChatRoom,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "device_id", nullable = false)
    val device: Device,

    @Column(nullable = false)
    val deviceStatus: Boolean
)