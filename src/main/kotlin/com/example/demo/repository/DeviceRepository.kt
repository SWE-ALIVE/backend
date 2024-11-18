package com.example.demo.repository

import com.example.demo.model.Device
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DeviceRepository : JpaRepository<Device, UUID> {
    fun findByProductNumber(productNumber: String): Optional<Device>
}
