package com.example.demo.service

import com.example.demo.dto.LoginRequestDTO
import com.example.demo.dto.user.UserCreateRequestDTO
import com.example.demo.dto.user.UserDTO
import com.example.demo.exception.UserNotFoundException
import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {

    fun login(request: LoginRequestDTO): UserDTO {
        // 전화번호와 비밀번호로 사용자 조회. 없으면 예외 발생
        val user = userRepository.findByPhoneNumberAndPassword(
            request.phoneNumber, request.password
        ).orElseThrow {
            UserNotFoundException("User with phone number '${request.phoneNumber}' and the given password isn't matched")
        }

        // 조회된 사용자 정보를 UserResponseDTO로 변환하여 반환
        return UserDTO(
            id = user.id,
            name = user.name,
            birthDate = user.birthDate,
            phoneNumber = user.phoneNumber
        )
    }

    @Transactional
    fun createUser(request: UserCreateRequestDTO): User {
        val user = User(
            id = UUID.randomUUID(),
            name = request.name,
            password = request.password,
            birthDate = request.birthDate,
            phoneNumber = request.phoneNumber
        )

        return userRepository.save(user)
    }

    @Transactional
    fun deleteUser(id: UUID) {
        if (!userRepository.existsById(id)) {
            throw UserNotFoundException("User with id $id not found")
        }
        userRepository.deleteById(id)
    }

    fun getAllUsers(): List<UserDTO> {
        val users = userRepository.findAll()

        return users.map { user ->
            UserDTO(
                id = user.id,
                name = user.name,
                birthDate = user.birthDate,
                phoneNumber = user.phoneNumber,
            )
        }
    }

    fun getUser(userId: UUID): User {
        // Optional이 비어있는 경우 UserNotFoundException을 던지게 설정
        return userRepository.findById(userId).orElseThrow {
            UserNotFoundException("User with id $userId not found")
        }
    }

    // 전화번호로 사용자 찾기
    fun getUserByPhoneNumber(phoneNumber: String): Optional<User> {
        return userRepository.findByPhoneNumber(phoneNumber)
    }


    // 사용자 정보 업데이트
    @Transactional
    fun updateUser(id: UUID, updatedUser: User): Optional<User> {
        return userRepository.findById(id).map {
            it.copy(
                name = updatedUser.name,
                password = updatedUser.password,
                birthDate = updatedUser.birthDate,
                phoneNumber = updatedUser.phoneNumber
            ).also { updated -> userRepository.save(updated) }
        }
    }
}
