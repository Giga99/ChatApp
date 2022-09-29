package com.medium.client.data.remote.responses

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val id: String,
    val user1: String,
    val user2: String,
    val lastMessage: MessageResponse
)
