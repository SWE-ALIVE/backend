package com.example.demo.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "channel")
data class Channel(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // 순환 참조의 끝
    val user: User,

    @Column(nullable = false)
    val name: String,

    @OneToMany(mappedBy = "channel", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    val chats: MutableList<Chat> = mutableListOf(),

    @OneToMany(mappedBy = "channel", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    val channelDevices: MutableList<ChannelDevice> = mutableListOf()
) {
    fun addDevice(device: Device) {
        val channelDevice = ChannelDevice(
            id = UUID.randomUUID(),
            channel = this, // 현재 채널 참조
            device = device,
            deviceStatus = true
        )
        this.channelDevices.add(channelDevice)
    }
}