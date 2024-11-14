package com.example.demo.controller

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

    @PostMapping("/user")
    fun createUser(
        @RequestParam userId: String,
        @RequestParam nickname: String
    ): ResponseEntity<String> {
        return sendbirdService.createUser(userId, nickname)
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
    fun getOpenChannels(): ResponseEntity<List<String>> {
        val channelUrls = sendbirdService.getOpenChannels()
        return ResponseEntity.ok(channelUrls)
    }
}
