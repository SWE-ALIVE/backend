package com.example.demo.controller.sendbird

import com.example.demo.dto.sendbird.SendbirdUserInviteRequest
import com.example.demo.service.sendbird.SendbirdChannelService

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/sendbird/channels")
class SendbirdChannelController(
    private val sendbirdChannelService: SendbirdChannelService
) {
    @GetMapping
    fun getGroupChannelsByUserId(
        @RequestParam userId: String
    ): ResponseEntity<String> {
        return sendbirdChannelService.getGroupChannelsByUserId(userId)
    }

    @DeleteMapping
    fun deleteGroupChannel(
        @RequestParam channelUrl: String
    ): ResponseEntity<String> {
        return sendbirdChannelService.deleteGroupChannel(channelUrl)
    }

    @PostMapping("/users")
    fun addUserToChannel(
        @RequestBody sendbirdUserInviteRequest: SendbirdUserInviteRequest
    ): ResponseEntity<String> {
        return sendbirdChannelService.addUsersToChannel(
            sendbirdUserInviteRequest.channelUrl,
            sendbirdUserInviteRequest.userIds
        )
    }
}
