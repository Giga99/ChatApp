package com.medium.data.message

interface MessageDataSource {

    suspend fun getAllMessages(user1: String, user2: String): List<Message>

    suspend fun getLastMessage(user1: String, user2: String): Message

    suspend fun insertMessage(message: Message): Boolean
}
