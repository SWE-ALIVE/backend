package com.example.demo.model

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "calendar")
data class Calendar(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    val user: User,


    @Column(nullable = false)
    @Size(max = 20, message = "실행 내용은 최대 20글자까지 가능합니다.")
    val content: String,

    @Column(name = "started_at", nullable = false)
    val startedAt: LocalDateTime,

    @Column(name = "finished_at", nullable = false)
    val finishedAt: LocalDateTime
)