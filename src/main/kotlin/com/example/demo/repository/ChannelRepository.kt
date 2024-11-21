package com.example.demo.repository

import com.example.demo.model.Channel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChannelRepository : JpaRepository<Channel, UUID> {
    abstract fun existsByProductNumber(productNumber: String): Boolean
}