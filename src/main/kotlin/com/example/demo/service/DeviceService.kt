package com.example.demo.service

import com.example.demo.dto.*
import com.example.demo.exception.DeviceNotFoundInChatRoomException
import com.example.demo.repository.ChatRoomDeviceRepository
import com.example.demo.repository.DeviceRepository
import com.example.demo.repository.DeviceUsageRecordRepository
import com.example.demo.repository.UserDeviceRepository
import main.kotlin.com.example.demo.model.DeviceUsageRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeviceService(
    @Autowired private val deviceUsageRecordRepository: DeviceUsageRecordRepository,
    @Autowired private val chatRoomDeviceRepository: ChatRoomDeviceRepository
) {

    fun updateDeviceStatus(chatroomId: UUID, deviceId: UUID, deviceStatus: Boolean): Boolean {
        // ChatRoomDevice를 찾는다 (chatroomId, deviceId로 찾기)
        val chatRoomDevice = chatRoomDeviceRepository.findByChatRoomIdAndDeviceId(chatroomId, deviceId)
            ?: throw DeviceNotFoundInChatRoomException("Device with ID $deviceId not found in chatroom $chatroomId")

        // 장치 상태 업데이트
        chatRoomDevice.deviceStatus = deviceStatus

        // 변경된 상태를 저장
        chatRoomDeviceRepository.save(chatRoomDevice)

        return true
    }

    fun getDeviceUsageRecords(request: DeviceUsageRequestDTO): ResponseEntity<DeviceUsageResponseDTO> {
        val records = deviceUsageRecordRepository.findByUserIdAndDeviceID(
            request.userId,
            request.deviceId
        )

        if (records.isEmpty()) {
            throw NoSuchElementException("No records found for userId: ${request.userId}, deviceId: ${request.deviceId}")
        }

        // 기기 가져오기
        val device = records.first().userDevice.device

        // 사용자와 기기를 기준으로 채팅방 필터링
        val chatRooms = device.chatRoomDevices
            .filter { chatRoomDevice -> chatRoomDevice.chatRoom.user.id == request.userId }
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

        // ResponseEntity로 반환
        return ResponseEntity.ok(response)
    }
}
