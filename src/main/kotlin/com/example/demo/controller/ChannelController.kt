package com.example.demo.controller

import com.example.demo.dto.ChannelDeviceDTO
import com.example.demo.dto.ChannelResponseDTO
import com.example.demo.dto.UserInviteRequestDTO
import com.example.demo.dto.sendbird.SendbirdChannelCreateRequest
import com.example.demo.exception.UserNotFoundException
import com.example.demo.model.Channel
import com.example.demo.model.ChannelDevice
import com.example.demo.service.ChannelService
import com.example.demo.service.UserService
import com.example.demo.service.sendbird.SendbirdChannelService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/channels")
class ChannelController(
    private val userService: UserService,
    private val sendbirdChannelService: SendbirdChannelService,
    private val channelService: ChannelService
) {
    @PostMapping
    fun createChannel(
        @RequestBody sendbirdChannelCreateRequest: SendbirdChannelCreateRequest
    ): ResponseEntity<Channel> {
        sendbirdChannelService.createGroupChannel(sendbirdChannelCreateRequest)

        return ResponseEntity(channelService.createChannel(sendbirdChannelCreateRequest), HttpStatus.CREATED)
    }

    @DeleteMapping
    fun deleteChannel(
        @RequestParam channelId: String
    ): ResponseEntity<String> {
        return try {
            sendbirdChannelService.deleteGroupChannel(channelId)
            channelService.deleteChannel(UUID.fromString(channelId))
            ResponseEntity<String>("channel deleted successfully", HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity<String>(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/{userId}")
    fun getUserChannels(@PathVariable userId: String): ResponseEntity<List<ChannelResponseDTO>> {
        // 유저 정보 가져오기
        return try {
            val user = userService.getUser(UUID.fromString(userId))
            // 유저가 존재하면 해당 유저의 channels 정보를 가져와서 반환
            val channelsDTO = user.channels.map { channel ->
                // 채팅방 이름과 연결된 장치들을 DTO로 변환
                val devices = channel.channelDevices.map { it.device.productNumber }
                ChannelResponseDTO(channel.name, devices)
            }
            ResponseEntity.ok(channelsDTO)
        } catch (e: UserNotFoundException) {
            // 유저가 존재하지 않으면 404 반환
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/{channelId}/users")
    fun getUsersInChannel(
        @PathVariable channelId: String
    ): ResponseEntity<List<ChannelDeviceDTO>> {

        return ResponseEntity(sendbirdChannelService.getUsersInChannel(channelId), HttpStatus.OK)
    }

    @PostMapping("/users")
    fun addContributorToChannel(
        @RequestBody request: UserInviteRequestDTO
    ): ResponseEntity<ChannelDevice> {
        sendbirdChannelService.addUsersToChannel(request.channelId, request.deviceIds)
        channelService.addContributorsToChannel(request)

        return ResponseEntity(HttpStatus.CREATED)
    }
}