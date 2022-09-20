package com.medium.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: String,
    val text: String,
    val sender: String,
    val receiver: String,
    val timestamp: Long
)
