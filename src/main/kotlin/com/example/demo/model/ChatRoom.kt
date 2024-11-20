package com.example.demo.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "chat_room")
data class ChatRoom(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // 순환 참조의 끝
    val user: User,

    @Column(nullable = false)
    val name: String,

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val chats: MutableList<Chat> = mutableListOf(),

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val chatRoomDevices: MutableList<ChatRoomDevice> = mutableListOf()
)