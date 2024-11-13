package com.example.demo.Model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "chat")
data class Chat(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(name = "chatroom_id", nullable = false)
    val chatRoomId: UUID,

    @Column(nullable = false)
    val content: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime
)
