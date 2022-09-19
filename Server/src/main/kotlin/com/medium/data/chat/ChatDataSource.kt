package com.medium.data.chat

interface ChatDataSource {

    suspend fun getAllMessages(user1: String, user2: String): List<Message>

    suspend fun insertMessage(message: Message): Boolean
}
