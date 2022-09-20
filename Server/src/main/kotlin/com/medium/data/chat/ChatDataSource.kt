package com.medium.data.chat

interface ChatDataSource {

    suspend fun getAllChats(username: String): List<Chat>

    suspend fun getChat(user1: String, user2: String): Chat?

    suspend fun insertChat(chat: Chat): Boolean
}
