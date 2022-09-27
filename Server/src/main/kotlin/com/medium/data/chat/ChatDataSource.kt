package com.medium.data.chat

import org.bson.types.ObjectId

interface ChatDataSource {

    suspend fun getAllChats(username: String): List<Chat>

    suspend fun getChat(user1: String, user2: String): Chat?

    suspend fun getChat(chatId: ObjectId): Chat?

    suspend fun insertChat(chat: Chat): Boolean
}
