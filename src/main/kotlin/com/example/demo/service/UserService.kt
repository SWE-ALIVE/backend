package com.example.demo.service

import com.example.demo.dto.LoginRequestDTO
import com.example.demo.dto.UserResponseDTO
import com.example.demo.exception.UserNotFoundException
import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {

    fun login(request: LoginRequestDTO): UserResponseDTO {
        // 전화번호와 비밀번호로 사용자 조회. 없으면 예외 발생
        val user = userRepository.findByPhoneNumberAndPassword(
            request.phoneNumber, request.password
        ).orElseThrow {
            UserNotFoundException("User with phone number '${request.phoneNumber}' and the given password isn't matched")
        }

        // 조회된 사용자 정보를 UserResponseDTO로 변환하여 반환
        return UserResponseDTO(
            id = user.id.toString(),
            name = user.name,
            birthDate = user.birthDate,
            phoneNumber = user.phoneNumber
        )
    }


    // 사용자 목록 가져오기
    @Transactional
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
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

    // 사용자 삭제
    fun deleteUser(id: UUID): Boolean {
        return userRepository.findById(id).map {
            userRepository.delete(it)
            true
        }.orElse(false)
    }
}
