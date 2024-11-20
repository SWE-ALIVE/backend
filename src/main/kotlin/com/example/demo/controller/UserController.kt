package com.example.demo.controller

import com.example.demo.dto.ChatRoomResponseDTO
import com.example.demo.dto.UserDTO
import com.example.demo.dto.UserDeviceDTO
import com.example.demo.exception.UserNotFoundException
import com.example.demo.model.User
import com.example.demo.service.UserService
import com.example.demo.service.sendbird.SendbirdUserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1")
class UserController(
    private val userService: UserService,
    private val sendbirdUserService: SendbirdUserService
) {

    @PostMapping("/SignUp")
    fun createUser(@RequestBody request: User): User {
        sendbirdUserService.createUser(request.id.toString(), request.name, "")
        return userService.createUser(request)
    }

    // 모든 사용자 조회
    @GetMapping("/users")
    fun getAllUsers(): List<UserDTO> {
        // User 리스트를 가져오고 DTO로 변환
        val users: List<User> = userService.getAllUsers()

        return users.map { user ->
            UserDTO(
                id = user.id,
                name = user.name,
                birthDate = user.birthDate,
                phoneNumber = user.phoneNumber,
            )
        }
    }

    @PostMapping("/user")
    fun getUser(@RequestBody request: String): UserDTO {
        // User 리스트를 가져오고 DTO로 변환
        val user: User = userService.getUserByPhoneNumber(request).get()

        return UserDTO(
            id = user.id,
            name = user.name,
            birthDate = user.birthDate,
            phoneNumber = user.phoneNumber,
        )
    }

    @DeleteMapping("/users/{userId}")
    fun deleteUser(
        @PathVariable userId: UUID
    ): ResponseEntity<String> {
        return try {
            sendbirdUserService.deleteUser(userId.toString())
            userService.deleteUser(userId)
            ResponseEntity<String>("User deleted successfully", HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        }
    }


    @GetMapping("/users/{userId}/devices")
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
}
