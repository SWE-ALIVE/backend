package com.example.demo.controller

import com.example.demo.dto.ChatRoomResponseDTO
import com.example.demo.exception.UserNotFoundException
import com.example.demo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/v1/channels")
class ChatRoomController(
    private val userService: UserService,

) {

    @GetMapping
    fun getUserChatRooms(@RequestParam userId: UUID): ResponseEntity<List<ChatRoomResponseDTO>> {

        // 유저 정보 가져오기
        return try {
            val user = userService.getUser(userId)  // 유저 조회 (UserNotFoundException 예외 발생 가능)

            // 유저가 존재하면 해당 유저의 chatRooms 정보를 가져와서 반환
            val chatRoomsDTO = user.chatRooms.map { chatRoom ->
                // 채팅방 이름과 연결된 장치들을 DTO로 변환
                val devices = chatRoom.chatRoomDevices.map { it.device.productNumber }
                ChatRoomResponseDTO(chatRoom.name, devices)
            }
            ResponseEntity.ok(chatRoomsDTO)
        } catch (e: UserNotFoundException) {
            // 유저가 존재하지 않으면 404 반환
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}