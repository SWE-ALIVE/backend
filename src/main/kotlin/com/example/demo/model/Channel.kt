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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    val user: User,

    @Column(nullable = false)
    val name: String,

    @OneToMany(mappedBy = "channel", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    val channelDevices: MutableList<ChannelDevice> = mutableListOf()
)
{
    fun addDevice(device: Device) {
        val channelDevice = ChannelDevice(
            id = UUID.randomUUID(),
            channel = this,
            device = device,
            deviceStatus = true
        )
        this.channelDevices.add(channelDevice)
    }
}