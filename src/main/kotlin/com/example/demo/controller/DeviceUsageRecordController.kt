package com.example.demo.controller

import com.example.demo.dto.DeviceUsageRequestDTO
import com.example.demo.dto.DeviceUsageResponseDTO
import com.example.demo.exception.DeviceNotInProgressException
import com.example.demo.service.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/device-usage")
class DeviceUsageRecordController(
    @Autowired private val deviceService: DeviceService
) {
    @PostMapping
    fun getDeviceUsageRecords(
        @RequestBody request: DeviceUsageRequestDTO
    ): ResponseEntity<DeviceUsageResponseDTO> {
        // 서비스에서 DeviceUsageResponseDTO를 가져옵니다.
        return deviceService.getDeviceUsageRecords(request)
    }
}