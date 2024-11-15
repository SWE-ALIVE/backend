package com.example.demo.model

import jakarta.persistence.*
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDateTime
import java.time.LocalDateTime.*
import java.time.LocalTime
import java.util.*

@Entity
@Table(name = "user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @Column(nullable = false)
    @Size(max = 10, message = "이름은 최대 10글자까지 가능합니다.")
    val name: String,

    @Column(nullable = false)
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$",
        message = "비밀번호는 대문자, 소문자, 숫자, 특수문자 중 3가지 이상을 포함해야 합니다."
    )
    val password: String,

    @Column(nullable = false)
    val birthDate: LocalTime,

    @Column(nullable = false, unique = true)
    val phoneNumber: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = now(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val userDevices: MutableList<UserDevice> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val chatRooms: MutableList<ChatRoom> = mutableListOf()
)