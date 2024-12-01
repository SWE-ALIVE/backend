package com.example.demo.controller

import com.example.demo.dto.channel.QueryMessagesRequest
import com.example.demo.dto.channel.SendMessageRequest
import com.example.demo.service.MessageService

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/sendbird/messages")
class MessageController(
    private val messageService: MessageService
) {
    // 채널에 메시지 보내기
    @PostMapping
    fun sendMessagesToChannel(
        @RequestBody request: SendMessageRequest
    ): ResponseEntity<String> {
        return messageService.sendMessagesToChannel(
            request.channelUrl,
            request.userId,
            request.message
        )
    }

    // 채널의 메시지를 가져오는 것
    @PostMapping("/query")
    fun queryMessagesFromChannel(
        @RequestBody request: QueryMessagesRequest
    ): ResponseEntity<String> {
        return messageService.queryMessagesFromChannel(
            request.channelUrl,
            request.messageTs,
            request.limit
        )
    }
}
