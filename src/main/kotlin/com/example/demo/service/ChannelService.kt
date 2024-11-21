package com.example.demo.service

import com.example.demo.dto.ChannelDeviceDTO
import com.example.demo.dto.CreateChannelRequest
import com.example.demo.dto.UserInviteRequestDTO
import com.example.demo.model.Channel
import com.example.demo.repository.ChannelDeviceRepository
import com.example.demo.repository.ChannelRepository
import com.example.demo.repository.DeviceRepository
import com.example.demo.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChannelService(
    private val channelRepository: ChannelRepository,
    private val userRepository: UserRepository,
    private val deviceRepository: DeviceRepository,
    private val channelDeviceRepository: ChannelDeviceRepository
) {

    @Transactional
    fun createChannel(request: CreateChannelRequest): Channel {
        val user = userRepository.findById(UUID.fromString(request.operatorIds.first()))
            .orElseThrow { IllegalArgumentException("User with ID ${request.operatorIds.first()} not found") }

        val channel = Channel(
            id = UUID.randomUUID(),
            user = user,
            name = request.name,
        )

        // 장치 추가
        for (deviceId in request.deviceIds) {
            val device = deviceRepository.findById(UUID.fromString(deviceId)).orElse(null)
            if (device != null)
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

    fun getContributorsInChannel(channelId: String): List<ChannelDeviceDTO> {
        val channelDevices = channelDeviceRepository.findByChannelId(UUID.fromString(channelId))
        return channelDevices.map { channelDevice ->
            ChannelDeviceDTO(
                id = channelDevice.device.id.toString(),
                name = channelDevice.device.productNumber,
                category = channelDevice.device.category.toString()
            )
        }
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
        channelRepository.save(channel)
    }
}
