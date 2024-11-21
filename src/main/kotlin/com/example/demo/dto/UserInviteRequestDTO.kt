package com.example.demo.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class UserInviteRequestDTO (

    @JsonProperty("channel_id")
    val channelId: UUID,

    @JsonProperty("device_id")
    val deviceId: UUID
)