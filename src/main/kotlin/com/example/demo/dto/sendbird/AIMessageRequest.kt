package com.example.demo.dto.sendbird

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.hibernate.validator.constraints.URL

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AIMessageRequest(
    val channelUrl: String, // 채널 URL
    val userId: String, // 유저 ID
    val dialog: DialogRequest, // 유저 채팅 + 기기 채팅
    val slotValues: List<ContextDTO> // 기기, 상태, 정보
)
