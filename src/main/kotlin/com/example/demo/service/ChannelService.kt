package com.example.demo.service

import com.example.demo.dto.UserInviteRequestDTO
import com.example.demo.dto.sendbird.SendbirdChannelCreateRequest
import com.example.demo.model.Channel
import com.example.demo.model.ChannelDevice
import com.example.demo.repository.*
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChannelService(
    private val channelRepository: ChannelRepository,
    private val userRepository: UserRepository,
    private val deviceRepository: DeviceRepository
) {

    @Transactional
    fun createChannel(request: SendbirdChannelCreateRequest): Channel {
        val user = userRepository.findById(UUID.fromString(request.operatorIds.first()))
            .orElseThrow { IllegalArgumentException("User with ID ${request.operatorIds.first()} not found") }

        val channel = Channel(
            id = UUID.randomUUID(),
            user = user,
            name = request.name,
        )
        // 장치 추가
        for (deviceId in request.deviceIds) {
            val device = deviceRepository.findById(UUID.fromString(deviceId))
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
    fun addContributorsToChannel(requestDTO: UserInviteRequestDTO) {
        val channelId = UUID.fromString(requestDTO.channelId)
        val deviceIds = requestDTO.deviceIds.map { UUID.fromString(it) }

        val channel = channelRepository.findById(channelId)
            .orElseThrow { NoSuchElementException("Channel with ID $channelId not found") }

        // 장치들 조회
        val devices = deviceRepository.findAllById(deviceIds)

        // 모든 장치가 존재하는지 확인
        val foundDeviceIds = devices.map { it.id }.toSet()
        val missingDeviceIds = deviceIds.toSet() - foundDeviceIds

        if (missingDeviceIds.isNotEmpty()) {
            throw NoSuchElementException("Devices with IDs $missingDeviceIds not found")
        }

        // 장치들을 채널에 추가
        devices.forEach { device ->
            channel.addDevice(device)
        }
    }
}
