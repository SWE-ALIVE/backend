package com.example.demo.repository

import com.example.demo.model.DeviceUsageRecord
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DeviceUsageRecordRepository : JpaRepository<DeviceUsageRecord, UUID> {
    fun findByUserDeviceId(userDeviceId: UUID): List<DeviceUsageRecord>
}