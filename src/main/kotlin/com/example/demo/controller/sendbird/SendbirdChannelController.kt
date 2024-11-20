package com.example.demo.controller.sendbird

import com.example.demo.dto.sendbird.SendbirdChannelCreateRequest
import com.example.demo.dto.sendbird.SendbirdUserInviteRequest
import com.example.demo.service.sendbird.SendbirdChannelService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/sendbird/channels")
class SendbirdChannelController(
    private val sendbirdChannelService: SendbirdChannelService
) {

    // 전체 가져오기
    @GetMapping
    fun getGroupChannels(): ResponseEntity<String> {
        return sendbirdChannelService.getGroupChannels()
    }

    @PostMapping
    fun createGroupChannel(
        @RequestBody sendbirdChannelCreateRequest: SendbirdChannelCreateRequest
    ): ResponseEntity<String> {
        return sendbirdChannelService.createGroupChannel(sendbirdChannelCreateRequest)
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
        return sendbirdChannelService.addUserToChannel(
            sendbirdUserInviteRequest.channelUrl,
            sendbirdUserInviteRequest.userIds
        )
    }

    @GetMapping("/{channelUrl}/users")
    fun getUsersInChannel(
        @PathVariable channelUrl: String
    ): ResponseEntity<String> {
        return sendbirdChannelService.getUsersInChannel(channelUrl)
    }

}
