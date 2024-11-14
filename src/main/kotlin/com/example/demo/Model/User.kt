package com.example.demo.Model

import jakarta.persistence.*
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalTime
import java.util.*

@Entity
@Table(name = "user")
data class User(
    @Id
    val id: UUID = UUID.randomUUID(),

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

    @Column(name = "birth_date", nullable = false)
    val birthDate: LocalTime,

    @Column(name = "phone_number", nullable = false)
    val phoneNumber: String,

    // 나의 채팅방 목록만 뽑아 올 수 있음.
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val chatRooms: List<ChatRoom> = mutableListOf(),

    // 활동 내역만 뽑아 올 수 있음.
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val calendars: List<Calendar> = mutableListOf()

)