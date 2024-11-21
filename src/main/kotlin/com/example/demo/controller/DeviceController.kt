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
        return try {
            sendbirdUserService.deleteUser(deviceId.toString())
            deviceService.deleteDevice(deviceId)
            ResponseEntity<String>("Device deleted successfully", HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/users/{userId}")
    fun getUserDevices(@PathVariable userId: UUID): ResponseEntity<List<UserDeviceDTO>> {
        return try {
            val user = userService.getUser(userId)  // 유저 조회 (UserNotFoundException 예외 발생 가능)

            // 유저가 존재하면 해당 유저의 userDevices 정보를 가져와서 반환
            val userDevicesDTO = user.userDevices.map { userDevice ->
                // UserDevice를 통해 DeviceDTO로 변환
                UserDeviceDTO(
                    category = userDevice.device.category.name,  // DeviceCategory의 이름
                    deviceId = userDevice.device.id,  // Device의 ID
                    deviceName = userDevice.device.productNumber // 장치 이름
                )
            }

            // 유저의 장치 목록 반환
            ResponseEntity.ok(userDevicesDTO)
        } catch (e: UserNotFoundException) {
            // 유저가 존재하지 않으면 400 반환
            ResponseEntity.badRequest().build()
        }
    }

    @PatchMapping("/status")
    fun updateDeviceStatus(@RequestBody request: DeviceStatusRequestDTO): ResponseEntity<String> {
        // 요청 본문에서 받은 데이터로 상태 업데이트
        return try {
            // 서비스 레이어에서 장치 상태를 업데이트하도록 처리
            deviceService.updateDeviceStatus(request.channelId, request.deviceId, request.deviceStatus)
            return ResponseEntity.ok("Device status updated successfully")

        } catch (e: DeviceNotFoundInChannelException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}
