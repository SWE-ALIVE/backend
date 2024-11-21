package com.example.demo.controller

import com.example.demo.dto.DeviceUsageRequestDTO
import com.example.demo.dto.DeviceUsageResponseDTO
import com.example.demo.service.DeviceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/device-usage")
class DeviceUsageRecordController(
    private val deviceService: DeviceService
) {
    @PostMapping
    fun getDeviceUsageRecords(
        @RequestBody request: DeviceUsageRequestDTO
    ): ResponseEntity<DeviceUsageResponseDTO> {
        // 서비스에서 DeviceUsageResponseDTO를 가져옵니다.
        return ResponseEntity(deviceService.getDeviceUsageRecords(request), HttpStatus.OK)
    }
}