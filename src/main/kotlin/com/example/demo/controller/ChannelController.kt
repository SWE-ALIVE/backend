package com.example.demo.controller

import com.example.demo.dto.channel.ChannelDeviceDTO
import com.example.demo.dto.channel.ChannelResponseDTO
import com.example.demo.dto.channel.CreateChannelRequest
import com.example.demo.dto.user.UserInviteRequestDTO
import com.example.demo.model.Channel
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
        @RequestBody request: CreateChannelRequest
    ): ResponseEntity<Channel> {
        val channel = channelService.createChannel(request)
        sendbirdChannelService.createGroupChannel(request, channel.id.toString())

        return ResponseEntity(HttpStatus.CREATED)
    }

    @DeleteMapping
    fun deleteChannel(
        @RequestParam("channel_id") channelId: String
    ): ResponseEntity<String> {
        sendbirdChannelService.deleteGroupChannel(channelId)
        channelService.deleteChannel(UUID.fromString(channelId))

        return ResponseEntity<String>("channel deleted successfully", HttpStatus.OK)
    }

    @GetMapping("/{userId}")
    fun getUserChannels(@PathVariable userId: String): ResponseEntity<List<ChannelResponseDTO>> {
        // 유저 정보 가져오기
        val user = userService.getUser(UUID.fromString(userId))
        // 유저가 존재하면 해당 유저의 channels 정보를 가져와서 반환
        val channelsDTO = user.channels.map { channel ->
            // 채팅방 이름과 연결된 장치들을 DTO로 변환
            val devices = channel.channelDevices.map { it.device.productNumber }
            ChannelResponseDTO(channel.id.toString(), channel.name, devices)
        }

        return ResponseEntity(channelsDTO, HttpStatus.OK)
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
    ): ResponseEntity<String> {
        channelService.addContributorsToChannel(request)
        sendbirdChannelService.addUsersToChannel(request.channelId, request.deviceIds)

        return ResponseEntity("Contributors added successfully", HttpStatus.OK)
    }
}