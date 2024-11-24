package com.example.demo.controller

import com.example.demo.dto.device.DeviceUsageCreateDTO
import com.example.demo.dto.device.DeviceUsageRequestDTO
import com.example.demo.dto.device.DeviceUsageResponseDTO
import com.example.demo.service.DeviceService
import com.example.demo.service.DeviceUsageRecordService

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/device-usage")
class DeviceUsageRecordController(
    private val deviceService: DeviceService,
    private val deviceUsageRecordService: DeviceUsageRecordService
) {
    @PostMapping("/add")
    fun addDeviceUsageRecord(
        @RequestBody request: DeviceUsageCreateDTO
    ): ResponseEntity<String> {
        deviceUsageRecordService.addDeviceUsageRecord(request)

        return ResponseEntity<String>("Record added successfully", HttpStatus.OK)
    }

    @PostMapping
    fun getDeviceUsageRecords(
        @RequestBody request: DeviceUsageRequestDTO
    ): ResponseEntity<DeviceUsageResponseDTO> {
        return ResponseEntity(deviceService.getDeviceUsageRecords(request), HttpStatus.OK)
    }
}