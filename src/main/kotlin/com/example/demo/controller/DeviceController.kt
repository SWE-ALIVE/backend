package com.example.demo.controller

import com.example.demo.dto.DeviceCreateRequestDTO
import com.example.demo.dto.DeviceStatusRequestDTO
import com.example.demo.dto.UserDeviceDTO
import com.example.demo.exception.DeviceNotFoundInChannelException
import com.example.demo.exception.UserNotFoundException
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
        sendbirdUserService.deleteUser(deviceId.toString())
        deviceService.deleteDevice(deviceId)

        return ResponseEntity<String>("Device deleted successfully", HttpStatus.OK)
    }

    @GetMapping("/users/{userId}")
    fun getUserDevices(@PathVariable userId: UUID): ResponseEntity<List<UserDeviceDTO>> {

        val user = userService.getUser(userId)  // 유저 조회 (UserNotFoundException 예외 발생 가능)

        // 유저가 존재하면 해당 유저의 userDevices 정보를 가져와서 반환
        return user.userDevices.map { userDevice ->
            // UserDevice를 통해 DeviceDTO로 변환
            UserDeviceDTO(
                category = userDevice.device.category.name,  // DeviceCategory의 이름
                deviceId = userDevice.device.id,  // Device의 ID
                deviceName = userDevice.device.productNumber // 장치 이름
            )
        }.let { ResponseEntity(it, HttpStatus.OK) }
    }

    @PatchMapping("/status")
    fun updateDeviceStatus(@RequestBody request: DeviceStatusRequestDTO): ResponseEntity<String> {
        deviceService.updateDeviceStatus(request.channelId, request.deviceId, request.deviceStatus)
        return ResponseEntity("Device status updated successfully", HttpStatus.OK)
    }

}
