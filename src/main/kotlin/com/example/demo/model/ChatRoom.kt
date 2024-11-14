package com.example.demo.model

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.util.UUID

@Entity
@Table(name = "chat_room")
data class ChatRoom(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val name: String,

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val chats: MutableList<Chat> = mutableListOf(),

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val chatRoomDevices: MutableList<ChatRoomDevice> = mutableListOf()
)