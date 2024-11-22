package com.example.demo.model

import com.fasterxml.jackson.annotation.JsonBackReference

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user_device")
data class UserDevice(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    @JsonBackReference
    val device: Device,

    @Column(nullable = false)
    val status: Boolean
)
