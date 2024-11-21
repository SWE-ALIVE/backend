package com.example.demo.repository

import com.example.demo.model.ChannelDevice
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChannelDeviceRepository : JpaRepository<ChannelDevice, UUID> {
    fun findByChannelIdAndDeviceId(channelId: UUID, deviceId: UUID): ChannelDevice?

    fun findByChannelId(channelId: UUID): List<ChannelDevice>
}
