package com.example.demo.repository

import com.example.demo.model.DeviceUsageRecord
import com.example.demo.model.UserDevice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserDeviceRepository : JpaRepository<UserDevice, UUID> {

    fun findUserDeviceByUserIdAndDeviceId(userId: UUID, deviceId: UUID): UserDevice
}