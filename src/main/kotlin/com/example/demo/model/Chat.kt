package com.example.demo.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "chat")
data class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY , cascade = [CascadeType.ALL])
    @JoinColumn(name = "chatroom_id", nullable = false)
    val chatRoom: ChatRoom,

    @Column(nullable = false)
    val content: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)