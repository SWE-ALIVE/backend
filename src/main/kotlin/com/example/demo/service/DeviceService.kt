package com.example.demo.service

import com.example.demo.dto.ActionDTO
import com.example.demo.dto.UserDeviceResponseDTO
import com.example.demo.dto.channel.ChannelDTO
import com.example.demo.dto.device.DeviceCreateRequestDTO
import com.example.demo.dto.device.DeviceStatusRequestDTO
import com.example.demo.dto.device.DeviceUsageRequestDTO
import com.example.demo.dto.device.DeviceUsageResponseDTO
import com.example.demo.exception.DeviceNotFoundInChannelException
import com.example.demo.exception.UserNotFoundException
import com.example.demo.model.Device
import com.example.demo.model.User
import com.example.demo.repository.ChannelDeviceRepository
import com.example.demo.repository.DeviceRepository
import com.example.demo.repository.DeviceUsageRecordRepository
import com.example.demo.repository.UserDeviceRepository

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
            name = request.name,
            nickname = request.nickname,
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

    fun getUserDevices(user: User): List<UserDeviceResponseDTO> {
        // 유저가 존재하면 해당 유저의 userDevices 정보를 가져와서 반환
        return user.userDevices.map { userDevice ->
            // UserDevice를 통해 DeviceDTO로 변환
            UserDeviceResponseDTO(
                category = userDevice.device.category.name,
                deviceId = userDevice.device.id,
                name = userDevice.device.name,
                nickname = userDevice.device.nickname
            )
        }
    }

    @Transactional
    fun updateDeviceStatus(device : DeviceStatusRequestDTO) {
        val channelDevice = channelDeviceRepository.findByChannelIdAndDeviceId(device.channelId, device.deviceId)
            ?: throw DeviceNotFoundInChannelException("Device with ID ${device.deviceId} not found in channel $device.channelId")
        
        // 장치 상태 업데이트
        channelDevice.deviceStatus = device.deviceStatus

        // 변경된 상태를 저장
         channelDeviceRepository.save(channelDevice)
    }

    fun getDeviceUsageRecords(request: DeviceUsageRequestDTO): DeviceUsageResponseDTO {

        val userDevice = userDeviceRepository.findUserDeviceByUserIdAndDeviceId(request.userId, request.deviceId)

        val records = deviceUsageRecordRepository.findByUserDeviceId(userDevice.id)

        if (records.isEmpty()) {
            throw NoSuchElementException("No records found for userDeviceId: ${userDevice.id}")
        }

        val device = records.first().userDevice.device

        // 사용자와 기기를 기준으로 채팅방 필터링
        val channels = device.channelDevices
            .filter { channelDevice -> channelDevice.channel.user.id == records.first().userDevice.user.id }
            .map { channelDevice ->
                ChannelDTO(
                    channelName = channelDevice.channel.name,
                    channelId = channelDevice.channel.id.toString(),
                    channelDevices = channelDevice.channel.channelDevices.map { it.device.nickname }
                )
            }

        // 사용 기록 정보 가져오기
        val actions = records.map { record ->
            ActionDTO(
                actionDescription = record.actionDescription,
                startTime = record.startTime,
                endTime = record.endTime
            )
        }

        val response = DeviceUsageResponseDTO(
            name = device.category.name,
            channels = channels,
            actions = actions
        )

        return response
    }

}
