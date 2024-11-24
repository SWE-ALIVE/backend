package com.example.demo.controller.sendbird

import com.example.demo.dto.sendbird.SendbirdUserCreateRequest
import com.example.demo.dto.sendbird.SendbirdUserCreateResponse
import com.example.demo.service.sendbird.SendbirdUserService

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/sendbird/users")
class SendbirdUserController(
    private val sendbirdUserService: SendbirdUserService
) {
    @GetMapping
    fun getUsers(): ResponseEntity<String> {
        return sendbirdUserService.getUsers()
    }

    @PostMapping
    fun createUser(
        @RequestBody sendbirdUserCreateRequest: SendbirdUserCreateRequest
    ): ResponseEntity<SendbirdUserCreateResponse> {
        return sendbirdUserService.createUser(
            sendbirdUserCreateRequest.userId,
            sendbirdUserCreateRequest.nickname,
            sendbirdUserCreateRequest.profileUrl
        )
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(
        @PathVariable userId: String
    ): ResponseEntity<String> {
        return sendbirdUserService.deleteUser(userId)
    }
}
