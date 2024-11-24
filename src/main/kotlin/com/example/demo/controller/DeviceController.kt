package com.example.demo.controller

import com.example.demo.dto.device.DeviceCreateRequestDTO
import com.example.demo.dto.device.DeviceStatusRequestDTO
import com.example.demo.dto.UserDeviceResponseDTO
import com.example.demo.model.Device
import com.example.demo.service.DeviceService
import com.example.demo.service.UserService
import com.example.demo.service.sendbird.SendbirdUserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/devices")
class DeviceController(
    private val deviceService: DeviceService,
    private val userService: UserService,
    private val sendbirdUserService: SendbirdUserService
) {
    @PostMapping
    fun createDevice(@RequestBody request: DeviceCreateRequestDTO): ResponseEntity<Device> {
        val device = deviceService.createDevice(request)
        sendbirdUserService.createUser(device.id.toString(), request.name, "")

        return ResponseEntity(device, HttpStatus.CREATED)
    }

    @DeleteMapping("/{deviceId}")
    fun deleteDevice(
        @PathVariable deviceId: UUID
    ): ResponseEntity<String> {
        deviceService.deleteDevice(deviceId)
        sendbirdUserService.deleteUser(deviceId.toString())

        return ResponseEntity<String>("Device deleted successfully", HttpStatus.OK)
    }

    @GetMapping("/users/{userId}")
    fun getUserDevices(@PathVariable userId: UUID): ResponseEntity<List<UserDeviceResponseDTO>> {
        val user = userService.getUser(userId)
        val userDevices = deviceService.getUserDevices(user)

        return ResponseEntity(userDevices, HttpStatus.OK)
    }

    @PatchMapping("/status")
    fun updateDeviceStatus(@RequestBody request: DeviceStatusRequestDTO): ResponseEntity<String> {
        deviceService.updateDeviceStatus(request)
        return ResponseEntity("Device status updated successfully", HttpStatus.OK)
    }

}
