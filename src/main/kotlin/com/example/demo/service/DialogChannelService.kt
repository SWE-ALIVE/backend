package com.example.demo.service

import com.example.demo.dto.channel.ChannelDeviceDTO
import com.example.demo.dto.channel.CreateChannelRequestDTO
import com.example.demo.model.Device
import com.example.demo.repository.DeviceRepository
import com.example.demo.util.ApiHelper
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class DialogChannelService(
    private val restTemplate: RestTemplate,
    private val apiHelper: ApiHelper,
    private val deviceRepository: DeviceRepository
) {
    fun getGroupChannelsByUserId(userId: String): ResponseEntity<String> {
        val url = apiHelper.buildUrl("users/$userId/my_group_channels")
        val request = HttpEntity<Any>(apiHelper.getHeaders())
        return restTemplate.exchange(url, HttpMethod.GET, request, String::class.java)
    }

    fun createGroupChannel(request: CreateChannelRequestDTO, channelId: String): ResponseEntity<String> {
        val url = apiHelper.buildUrl("group_channels")
        val body = mapOf(
            "name" to request.name,
            "channel_url" to channelId,
            "user_ids" to request.deviceIds,
            "operator_ids" to request.operatorIds,
            "cover_url" to "https://res.cloudinary.com/dvhm5zqi5/image/upload/v1731955612/rounded-lg_dmw0sr.png"
        )
        val entity = HttpEntity(body, apiHelper.getHeaders())
        return restTemplate.postForEntity(url, entity, String::class.java)
    }

    fun deleteGroupChannel(channelUrl: String): ResponseEntity<String> {
        val url = apiHelper.buildUrl("group_channels/$channelUrl")
        val request = HttpEntity<Any>(apiHelper.getHeaders())
        return restTemplate.exchange(url, HttpMethod.DELETE, request, String::class.java)
    }

    fun addUsersToChannel(channelUrl: String, userIds: List<String>): ResponseEntity<String> {
        val url = apiHelper.buildUrl("group_channels/$channelUrl/invite")
        val body = mapOf("user_ids" to userIds)
        val request = HttpEntity(body, apiHelper.getHeaders())
        return restTemplate.postForEntity(url, request, String::class.java)
    }

    fun getUsersInChannel(channelUrl: String): List<ChannelDeviceDTO> {
        val url = apiHelper.buildUrl("group_channels/$channelUrl/members")
        val request = HttpEntity<Any>(apiHelper.getHeaders())
        val response = restTemplate.exchange(url, HttpMethod.GET, request, String::class.java)

        if (response.statusCode == HttpStatus.OK) {
            val objectMapper = ObjectMapper()
            val root: JsonNode = objectMapper.readTree(response.body)

            // 모든 deviceId를 추출
            val deviceIds = root["members"].map { it["user_id"].asText() }
            val deviceMap = findDevicesByIds(deviceIds)

            return root["members"].mapNotNull { member ->
                val deviceId = member["user_id"].asText()
                val category = deviceMap[deviceId]?.category?.name ?: "User"
                // category가 "User"인 경우 null 반환하여 필터링
                if (category == "User") null
                else ChannelDeviceDTO(
                    id = deviceId,
                    name = member["nickname"].asText(),
                    category = category,
                    nickname = member["nickname"].asText() // 값이 안가져와짐
                )
            }
        } else {
            throw IllegalStateException("Failed to fetch members from channel: $channelUrl")
        }
    }


    private fun findDevicesByIds(deviceIds: List<String>): Map<String, Device> {
        val uuids = deviceIds.mapNotNull { id ->
            try {
                UUID.fromString(id) // String -> UUID 변환
            } catch (e: IllegalArgumentException) {
                null
            }
        }
        // 한 번의 쿼리로 모든 Device 조회
        val devices = deviceRepository.findAllById(uuids)
        return devices.associateBy { it.id.toString() }
    }


}
