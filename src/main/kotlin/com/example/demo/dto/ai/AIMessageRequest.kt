package com.example.demo.dto.ai

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AIMessageRequest(
    val channelUrl: String, // 채널 URL
    val dialog: DialogRequest, // 유저 채팅 + 기기 채팅
    val slotValues: List<ContextDTO> // 기기, 상태, 정보
)
