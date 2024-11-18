package com.example.demo.controller.sendbird

import com.example.demo.dto.sendbird.QueryMessagesRequest
import com.example.demo.dto.sendbird.SendMessageRequest
import com.example.demo.service.sendbird.SendbirdMessageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/sendbird/messages")
class SendbirdMessageController(
    private val sendbirdMessageService: SendbirdMessageService
) {

    @PostMapping
    fun sendMessagesToChannel(
        @RequestBody request: SendMessageRequest
    ): ResponseEntity<String> {
        return sendbirdMessageService.sendMessagesToChannel(
            request.channelUrl,
            request.userId,
            request.message
        )
    }

    @PostMapping("/query")
    fun queryMessagesFromChannel(
        @RequestBody request: QueryMessagesRequest
    ): ResponseEntity<String> {
        return sendbirdMessageService.queryMessagesFromChannel(
            request.channelUrl,
            request.messageTs,
            request.limit
        )
    }
}
