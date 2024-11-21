package com.example.demo.dto.sendbird

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SendbirdChannelCreateRequest(
    val name: String,
    val userIds: List<UUID>,
    val operatorIds: List<UUID>
)
