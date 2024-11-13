package com.example.demo.Model

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.util.UUID

@Entity
@Table(name = "chatRoom")
data class ChatRoom(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    val user: User,

    @Column(nullable = false)
    @Size(max = 10, message = "방이름은 최대 10글자까지 가능합니다.")
    val name: String,

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val chatRoomDevices: List<ChatRoomDevice> = mutableListOf()
)
