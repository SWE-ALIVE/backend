package com.example.demo.repository

import main.kotlin.com.example.demo.model.DeviceUsageRecord
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DeviceUsageRecordRepository : JpaRepository<DeviceUsageRecord, UUID> {

    fun findByUserIdAndDeviceID(
        userId: UUID,
        deviceId: UUID
    ): List<DeviceUsageRecord>
}