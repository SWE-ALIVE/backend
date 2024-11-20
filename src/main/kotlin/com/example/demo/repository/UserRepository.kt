package com.example.demo.repository

import com.example.demo.model.User
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {

    fun findByPhoneNumber(phoneNumber: String): Optional<User>

    fun findByPhoneNumberAndPassword(phoneNumber: String, password: String): Optional<User>

}
