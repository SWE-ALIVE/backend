package com.example.demo.controller

import com.example.demo.dto.*
import com.example.demo.service.SendbirdService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/sendbird")
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

    @PostMapping("/channels/users")
    fun addUserToChannel(
        @RequestBody sendbirdUserInviteRequest: SendbirdUserInviteRequest
    ): ResponseEntity<String> {
        return sendbirdService.addUserToChannel(sendbirdUserInviteRequest.channelUrl, sendbirdUserInviteRequest.userIds)
    }

    @PostMapping("/channels/messages")
    fun sendMessagesToChannel(
        @RequestBody request: SendMessageRequest
    ): ResponseEntity<String> {
        return sendbirdService.sendMessagesToChannel(request.channelUrl, request.userId, request.message)
    }

    @PostMapping("/channels/messages/query")
    fun queryMessagesFromChannel(
        @RequestBody request: QueryMessagesRequest
    ): ResponseEntity<String> {
        return sendbirdService.queryMessagesFromChannel(request.channelUrl, request.messageTs, request.limit)
    }

}
