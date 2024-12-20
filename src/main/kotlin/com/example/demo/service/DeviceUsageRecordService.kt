package com.example.demo.service

import com.example.demo.dto.device.DeviceUsageCreateDTO
import com.example.demo.model.DeviceUsageRecord
import com.example.demo.repository.DeviceUsageRecordRepository
import com.example.demo.repository.UserDeviceRepository
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.random.Random

@Service
class DeviceUsageRecordService(
    private val deviceUsageRecordRepository: DeviceUsageRecordRepository,
    private val userDeviceRepository: UserDeviceRepository
) {

    fun addDeviceUsageRecord(request: DeviceUsageCreateDTO) {
        val userDevice = userDeviceRepository.findUserDeviceByUserIdAndDeviceId(
            UUID.fromString(request.userId),
            UUID.fromString(request.deviceId)
        )
        val koreaZoneId = ZoneId.of("Asia/Seoul")
        val now = ZonedDateTime.now(koreaZoneId).toLocalDateTime()
        val randomMinutes = Random.nextInt(1, 60)
        val startTime = now.minusMinutes(randomMinutes.toLong())

        val record = DeviceUsageRecord(
            id = UUID.randomUUID(),
            userDevice = userDevice,
            actionDescription = request.actionDescription,
            startTime = startTime,
            endTime = now
        )

        deviceUsageRecordRepository.save(record)
    }
}