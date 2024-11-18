package com.example.demo.repository

import com.example.demo.model.ChatRoomDevice
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChatRoomDeviceRepository : JpaRepository<ChatRoomDevice, UUID> {
    fun findByChatRoomIdAndDeviceId(chatRoomId: UUID, deviceId: UUID): ChatRoomDevice?
}
