package com.example.demo.controller

import com.example.demo.dto.LoginRequestDTO
import com.example.demo.dto.UserResponseDTO
import com.example.demo.dto.user.UserDTO
import com.example.demo.exception.UserNotFoundException
import com.example.demo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthController(private val userService: UserService) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequestDTO): ResponseEntity<UserDTO> {
        return try {
            val userResponse: UserDTO = userService.login(request)
            ResponseEntity.ok(userResponse)
        } catch (e: UserNotFoundException) {
            // 사용자가 없으면 401 Unauthorized 반환
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        } catch (e: Exception) {
            // 그 외의 예외는 500 Internal Server Error 반환
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}