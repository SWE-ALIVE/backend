package com.example.demo.controller

import com.example.demo.dto.DeviceStatusRequestDTO
import com.example.demo.dto.DeviceUsageResponseDTO
import com.example.demo.exception.DeviceNotFoundInChatRoomException
import com.example.demo.exception.DeviceNotInProgressException
import com.example.demo.model.Device
import com.example.demo.service.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/devices")
class DeviceController(@Autowired private val deviceService: DeviceService) {

    @PatchMapping("/status")
    fun updateDeviceStatus(@RequestBody request: DeviceStatusRequestDTO): ResponseEntity<String> {
        // 요청 본문에서 받은 데이터로 상태 업데이트
        return try {
            // 서비스 레이어에서 장치 상태를 업데이트하도록 처리
            deviceService.updateDeviceStatus(request.chatroomId, request.deviceId, request.deviceStatus)
            return ResponseEntity.ok("Device status updated successfully")

        } catch (e: DeviceNotFoundInChatRoomException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}
