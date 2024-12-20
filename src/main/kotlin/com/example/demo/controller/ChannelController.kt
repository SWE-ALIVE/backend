package com.example.demo.controller

import com.example.demo.dto.channel.ChannelResponseDTO
import com.example.demo.dto.channel.CreateChannelRequestDTO
import com.example.demo.dto.user.UserInviteRequestDTO
import com.example.demo.model.Channel
import com.example.demo.service.ChannelService
import com.example.demo.service.UserService
import com.example.demo.service.DialogChannelService

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/channels")
class ChannelController(
    private val userService: UserService,
    private val sendbirdChannelService: DialogChannelService,
    private val channelService: ChannelService
) {
    @PostMapping
    fun createChannel(
        @RequestBody request: CreateChannelRequestDTO
    ): ResponseEntity<Channel> {
        val channel = channelService.createChannel(request)
        sendbirdChannelService.createGroupChannel(request, channel.id.toString())

        return ResponseEntity(channel, HttpStatus.CREATED)
    }

    @DeleteMapping
    fun deleteChannel(
        @RequestParam("channel_id") channelId: String
    ): ResponseEntity<String> {
        channelService.deleteChannel(UUID.fromString(channelId))
        sendbirdChannelService.deleteGroupChannel(channelId)

        return ResponseEntity<String>("channel deleted successfully", HttpStatus.OK)
    }

    @GetMapping("/users/{userId}")
    fun getUserChannels(@PathVariable userId: String): ResponseEntity<List<ChannelResponseDTO>> {
        // 유저 정보 가져오기
        val user = userService.getUser(UUID.fromString(userId))
        return ResponseEntity(channelService.getChannelsWithDevices(user), HttpStatus.OK)
    }

    @PostMapping("/users")
    fun addContributorToChannel(
        @RequestBody request: UserInviteRequestDTO
    ): ResponseEntity<String> {
        channelService.addContributorsToChannel(request)
        sendbirdChannelService.addUsersToChannel(request.channelId, request.deviceIds)

        return ResponseEntity("Contributors added successfully", HttpStatus.OK)
    }

    @GetMapping("/users/{userId}/details")
    fun getGroupChannelsByUserId(
        @PathVariable userId: String
    ): ResponseEntity<String> {
        return sendbirdChannelService.getGroupChannelsByUserId(userId)
    }
}