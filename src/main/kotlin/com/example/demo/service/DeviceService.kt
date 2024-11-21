package com.example.demo.service

import com.example.demo.dto.*
import com.example.demo.exception.DeviceNotFoundInChannelException
import com.example.demo.exception.UserNotFoundException
import com.example.demo.model.Device
import com.example.demo.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DeviceService(
    private val deviceUsageRecordRepository: DeviceUsageRecordRepository,
    private val channelDeviceRepository: ChannelDeviceRepository,
    private val userDeviceRepository: UserDeviceRepository,
    private val deviceRepository: DeviceRepository
) {

    @Transactional
    fun createDevice(request: DeviceCreateRequestDTO): Device {
        return Device(
            id = UUID.randomUUID(),
            productNumber = request.name,
            category = request.category,
            extraFunction = request.extraFunction
        ).let { deviceRepository.save(it) }
    }

    @Transactional
    fun deleteDevice(id: UUID) {
        if(!deviceRepository.existsById(id)) {
            throw UserNotFoundException("Device with id $id not found")
        }
        deviceRepository.deleteById(id)
    }

    @Transactional
    fun updateDeviceStatus(channelId: UUID, deviceId: UUID, deviceStatus: Boolean) {

        val channelDevice = channelDeviceRepository.findByChannelIdAndDeviceId(channelId, deviceId)
            ?: throw DeviceNotFoundInChannelException("Device with ID $deviceId not found in channel $channelId")
        
        // 장치 상태 업데이트
        channelDevice.deviceStatus = deviceStatus

        // 변경된 상태를 저장
        channelDeviceRepository.save(channelDevice)
    }

    fun getDeviceUsageRecords(request: DeviceUsageRequestDTO): DeviceUsageResponseDTO {

        val userDeviceId = userDeviceRepository.findUserDeviceByUserIdAndDeviceId(request.userId, request.deviceId)

        val records = deviceUsageRecordRepository.findByUserDeviceId(userDeviceId.id)

        if (records.isEmpty()) {
            throw NoSuchElementException("No records found for userDeviceId: ${userDeviceId.id}")
        }

        // 기기 가져오기
        val device = records.first().userDevice.device

        // 사용자와 기기를 기준으로 채팅방 필터링
        val channels = device.channelDevices
            .filter { channelDevice -> channelDevice.channel.user.id == records.first().userDevice.user.id }
            .map { channelDevice ->
                ChannelDTO(
                    channelName = channelDevice.channel.name,
                    channelDevices = channelDevice.channel.channelDevices.map { it.device.productNumber }
                )
            }

        // 사용 기록 정보 가져오기
        val actions = records.map { record ->
            ActionDTO(
                actionDescription = record.actionDescription,
                usageDate = record.usageDate,
                startTime = record.startTime.toLocalTime(),
                endTime = record.endTime.toLocalTime()
            )
        }

        val response = DeviceUsageResponseDTO(
            deviceName = device.category.name,
            channels = channels,
            actions = actions
        )

        return response
    }

}
