package com.example.demo.controller

import com.example.demo.dto.SendbirdChannelCreateRequest
import com.example.demo.dto.SendbirdUserCreateRequest
import com.example.demo.dto.SendbirdUserCreateResponse
import com.example.demo.dto.SendbirdUserInviteRequest
import com.example.demo.service.SendbirdService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sendbird")
class SendbirdController(
    private val sendbirdService: SendbirdService
) {
    @GetMapping("/test")
    fun test(): String {
        return "Hello, Sendbird!"
    }

    @GetMapping("/users")
    fun getUsers(): ResponseEntity<String> {
        return sendbirdService.getUsers()
    }

    @PostMapping("/users")
    fun createUser(
        @RequestBody sendbirdUserCreateRequest: SendbirdUserCreateRequest
    ): ResponseEntity<SendbirdUserCreateResponse> {
        return sendbirdService.createUser(sendbirdUserCreateRequest.userId, sendbirdUserCreateRequest.nickname, sendbirdUserCreateRequest.profileUrl)
    }

    @PostMapping("/message")
    fun sendMessage(
        @RequestParam channelUrl: String,
        @RequestParam userId: String,
        @RequestParam message: String
    ): ResponseEntity<String> {
        return sendbirdService.sendMessage(channelUrl, userId, message)
    }

    @GetMapping("/channels")
    fun getGroupChannels(): ResponseEntity<String> {
        return sendbirdService.getGroupChannels()
    }

    @PostMapping("/channels")
    fun createGroupChannel(
        @RequestBody sendbirdChannelCreateRequest: SendbirdChannelCreateRequest
    ): ResponseEntity<String> {
        return sendbirdService.createGroupChannel(sendbirdChannelCreateRequest)
    }

    @PostMapping("/channels/{channelUrl}/users")
    fun addUserToChannel(
        @PathVariable channelUrl: String,
        @RequestBody sendbirdUserInviteRequest: SendbirdUserInviteRequest
    ): ResponseEntity<String> {
        return sendbirdService.addUserToChannel(channelUrl, sendbirdUserInviteRequest.userIds)
    }

}
