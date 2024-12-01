package com.example.demo.controller

import com.example.demo.dto.user.UserCreateRequestDTO
import com.example.demo.dto.user.UserDTO
import com.example.demo.model.User
import com.example.demo.service.UserService
import com.example.demo.service.DialogUserService

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import java.util.*

@RestController
@RequestMapping("/v1")
class UserController(
    private val userService: UserService,
    private val sendbirdUserService: DialogUserService
) {
    @PostMapping("/signup")
    fun createUser(@RequestBody request: UserCreateRequestDTO): ResponseEntity<User> {
        val user = userService.createUser(request)
        sendbirdUserService.createUser(user.id.toString(), request.name, "")

        return ResponseEntity(user, HttpStatus.CREATED)
    }

    @DeleteMapping("/users/{userId}")
    fun deleteUser(
        @PathVariable userId: UUID
    ): ResponseEntity<String> {
        userService.deleteUser(userId)
        sendbirdUserService.deleteUser(userId.toString())

        return ResponseEntity<String>("User deleted successfully", HttpStatus.OK)
    }

    @GetMapping("/users")
    fun getAllUsers(): ResponseEntity<List<UserDTO>> {
        val users = userService.getAllUsers()

        return ResponseEntity(users, HttpStatus.OK)
    }
}
