package com.example.demo.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "chat")
data class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY , cascade = [CascadeType.ALL])
    @JoinColumn(name = "channel_id", nullable = false)
    @JsonBackReference
    val channel: Channel,

    @Column(nullable = false)
    val content: String,

    @Column(nullable = false)
    @JsonProperty("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)