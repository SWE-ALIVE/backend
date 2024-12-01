package com.example.demo.controller

import com.example.demo.dto.sendbird.AIMessageRequest
import com.example.demo.dto.sendbird.AIMessageResponse
import com.example.demo.service.MessageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/sendbird/ai")
class AIMessageController(
    private val messageService: MessageService
) {
    @PostMapping("/messages")
    fun sendAIMessage(
        @RequestBody request: AIMessageRequest
    ): ResponseEntity<AIMessageResponse> {
        val response = messageService.processAIMessage(request)
        return ResponseEntity(response, HttpStatus.OK)
    }
}
