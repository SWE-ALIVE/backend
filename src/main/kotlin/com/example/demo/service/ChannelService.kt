package com.example.demo.service

import com.example.demo.dto.ChannelCreateRequestDTO
import com.example.demo.model.Channel
import com.example.demo.model.ChannelDevice
import com.example.demo.model.Chat
import com.example.demo.model.Device
import com.example.demo.repository.*
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*
import kotlin.NoSuchElementException

@Service
class ChannelService(
    private val channelRepository: ChannelRepository,
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val deviceRepository: DeviceRepository,
    private val channelDeviceRepository: ChannelDeviceRepository
) {

    @Transactional
    fun createChannel(channelDTO : ChannelCreateRequestDTO): Channel {
        val user = userRepository.findById(channelDTO.operatorIds)
            .orElseThrow { IllegalArgumentException("User with ID ${channelDTO.operatorIds} not found") }

        val channel = Channel(
            id = UUID.randomUUID(),
            user = user,
            name = channelDTO.name,
        )
        // 장치 추가
        for (deviceId in channelDTO.productIds) {
            val device = deviceRepository.findById(deviceId)
                .orElseThrow { NoSuchElementException("Device with ID $deviceId not found") }

            channel.addDevice(device)
        }

        return channelRepository.save(channel)
    }

    @Transactional
    fun deleteChannel(id: UUID) {
        if (!channelRepository.existsById(id)) {
            throw NoSuchElementException("Channel with ID $id not found")
        }
        channelRepository.deleteById(id)
    }

    @Transactional
    fun addContributorToChannel(channelId: UUID, deviceId: UUID): ChannelDevice {
        // 채널 조회
        val channel = channelRepository.findById(channelId)
            .orElseThrow { NoSuchElementException("Channel with ID $channelId not found") }

        // 장치 조회
        val device = deviceRepository.findById(deviceId)
            .orElseThrow { NoSuchElementException("Device with ID $deviceId not found") }

        // 새로운 ChannelDevice 생성
        val channelDevice = ChannelDevice(
            id = UUID.randomUUID(),
            channel = channel,
            device = device,
            deviceStatus = true
        )
        return channelDeviceRepository.save(channelDevice)
    }

//    fun getDevicesInChannel(channelId: UUID): List<String> {
//        // 채널 조회
//        val channel = channelRepository.findById(channelId)
//            .orElseThrow { NoSuchElementException("Channel with ID $channelId not found") }
//
//        // 채널에 연결된 모든 Device 이름 반환
//        return channel.channelDevices.map { it.device.productNumber }
//    }
}
