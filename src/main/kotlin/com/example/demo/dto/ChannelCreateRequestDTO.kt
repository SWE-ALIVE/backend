package com.example.demo.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class ChannelCreateRequestDTO (

    val name: String,

    @JsonProperty("product_ids")
    val productIds: List<UUID>,

    @JsonProperty("operator_ids")
    val operatorIds: UUID
)
