package com.medium.client.domain.models.ui

data class ChatModel(
    val id: String,
    val user1: String,
    val user2: String,
    val lastMessage: MessageModel
)
