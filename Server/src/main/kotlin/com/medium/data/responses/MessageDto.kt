package com.medium.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: String,
    val text: String,
    val from: String,
    val to: String,
    val timestamp: Long
)
