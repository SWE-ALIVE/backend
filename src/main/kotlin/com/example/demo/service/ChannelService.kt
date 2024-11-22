package com.example.demo.service

import com.example.demo.dto.channel.ChannelDeviceDTO
import com.example.demo.dto.channel.ChannelResponseDTO
import com.example.demo.dto.channel.CreateChannelRequest
import com.example.demo.dto.user.UserInviteRequestDTO
import com.example.demo.model.Channel
import com.example.demo.model.Device
import com.example.demo.model.User
import com.example.demo.repository.ChannelDeviceRepository
import com.example.demo.repository.ChannelRepository
import com.example.demo.repository.DeviceRepository
import com.example.demo.repository.UserRepository
import com.example.demo.service.sendbird.SendbirdChannelService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*
import kotlin.NoSuchElementException

@Service
class ChannelService(
    private val channelRepository: ChannelRepository,
    private val userRepository: UserRepository,
    private val deviceRepository: DeviceRepository,
    private val channelDeviceRepository: ChannelDeviceRepository,
    private val sendbirdChannelService: SendbirdChannelService
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

    fun getChannelsWithDevices(user: User): List<ChannelResponseDTO> {
        return user.channels.map { channel ->
            // 채팅방 이름과 연결된 장치들을 DTO로 변환
            val devices = sendbirdChannelService.getUsersInChannel(channel.id.toString())
            devices.forEach { device ->
                val channelDevice = channel.channelDevices.find { it.device.id == UUID.fromString(device.id) }
                device.deviceStatus = channelDevice?.deviceStatus
            }
            ChannelResponseDTO(channel.id.toString(), channel.name, devices)
        }
    }

    @Transactional
    fun addContributorsToChannel(requestDTO: UserInviteRequestDTO) {
        val channelId = UUID.fromString(requestDTO.channelId)
        val deviceIds = requestDTO.deviceIds.map { UUID.fromString(it) }
        val channel = channelRepository.findById(channelId)
            .orElseThrow { NoSuchElementException("Channel with ID $channelId not found") }

        val devices = retrieveAndValidateDevices(channel, deviceIds)

        // 장치들을 채널에 추가
        devices.forEach { device ->
            channel.addDevice(device)
        }
        channelRepository.save(channel)
    }

    /**
     * 장치들을 조회하고, 존재 여부 및 중복 여부를 확인합니다.
     * 중복된 장치가 있으면 예외를 던집니다.
     * 존재하지 않는 장치가 있으면 예외를 던집니다.
     * 유효한 장치 리스트를 반환합니다.
     */
    private fun retrieveAndValidateDevices(channel: Channel, deviceIds: List<UUID>): List<Device> {
        // 요청된 모든 장치 조회
        val devices = deviceRepository.findAllById(deviceIds)

        // 존재하지 않는 장치 ID 확인
        val foundDeviceIds = devices.map { it.id }.toSet()
        val missingDeviceIds = deviceIds.toSet() - foundDeviceIds

        if (missingDeviceIds.isNotEmpty()) {
            throw NoSuchElementException("Devices with IDs $missingDeviceIds not found")
        }

        // 이미 채널에 속해있는 장치 ID 확인
        val existingDeviceIds = channel.channelDevices.map { it.device.id }.toSet()
        val duplicateDeviceIds = deviceIds.filter { it in existingDeviceIds }

        if (duplicateDeviceIds.isNotEmpty()) {
            throw IllegalArgumentException("Devices with IDs $duplicateDeviceIds are already part of the channel")
        }

        return devices
    }
}
