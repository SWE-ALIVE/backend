package com.example.demo.repository

import com.example.demo.model.Device
import com.example.demo.model.DeviceCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DeviceRepository : JpaRepository<Device, UUID> {
    /**
     * 도메인 카테고리에 해당하는 모든 장치를 조회합니다.
     */
    fun findByCategoryIn(categories: List<DeviceCategory>): List<Device>

    /**
     * 특정 카테고리에 해당하는 첫 번째 장치를 조회합니다.
     */
    fun findFirstByCategory(category: DeviceCategory): Device?
}
