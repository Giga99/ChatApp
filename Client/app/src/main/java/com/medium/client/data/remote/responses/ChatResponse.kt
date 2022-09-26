package com.medium.client.data.remote.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatResponse(
    val id: String,
    val user1: String,
    val user2: String,
    val lastMessage: MessageResponse
)
