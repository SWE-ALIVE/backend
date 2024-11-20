package com.example.demo.service

import com.example.demo.dto.ActionDTO
import com.example.demo.dto.ChatRoomDTO
import com.example.demo.dto.DeviceUsageRequestDTO
import com.example.demo.dto.DeviceUsageResponseDTO
import com.example.demo.exception.DeviceNotFoundInChatRoomException
import com.example.demo.repository.ChatRoomDeviceRepository
import com.example.demo.repository.DeviceRepository
import com.example.demo.repository.DeviceUsageRecordRepository
import com.example.demo.repository.UserDeviceRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeviceService(
    private val deviceUsageRecordRepository: DeviceUsageRecordRepository,
    private val chatRoomDeviceRepository: ChatRoomDeviceRepository,
    private val userDeviceRepository: UserDeviceRepository
) {

    fun updateDeviceStatus(chatroomId: UUID, deviceId: UUID, deviceStatus: Boolean) {
        // ChatRoomDevice를 찾는다 (chatroomId, deviceId로 찾기)
        val chatRoomDevice = chatRoomDeviceRepository.findByChatRoomIdAndDeviceId(chatroomId, deviceId)
            ?: throw DeviceNotFoundInChatRoomException("Device with ID $deviceId not found in chatroom $chatroomId")
        
        // 장치 상태 업데이트
        chatRoomDevice.deviceStatus = deviceStatus

        // 변경된 상태를 저장
        chatRoomDeviceRepository.save(chatRoomDevice)

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
        val chatRooms = device.chatRoomDevices
            .filter { chatRoomDevice -> chatRoomDevice.chatRoom.user.id == records.first().userDevice.user.id }
            .map { chatRoomDevice ->
                ChatRoomDTO(
                    chatRoomName = chatRoomDevice.chatRoom.name,
                    chatRoomDevices = chatRoomDevice.chatRoom.chatRoomDevices.map { it.device.productNumber }
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
            chatRooms = chatRooms,
            actions = actions
        )

        return response
    }

}
