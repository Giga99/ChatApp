package com.medium.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val id: String,
    val user1: String,
    val user2: String,
    val lastMessage: MessageDto
)
