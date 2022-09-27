package com.medium.data.chat

import org.bson.types.ObjectId
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.or

class MongoChatDataSource(
    db: CoroutineDatabase
) : ChatDataSource {

    private val chats = db.getCollection<Chat>()

    override suspend fun getAllChats(username: String): List<Chat> = chats.find(
        or(
            Chat::user1 eq username,
            Chat::user2 eq username
        )
    ).toList()

    override suspend fun getChat(user1: String, user2: String): Chat? = chats.findOne(
        or(
            and(Chat::user1 eq user1, Chat::user2 eq user2),
            and(Chat::user1 eq user2, Chat::user2 eq user1)
        )
    )

    override suspend fun getChat(chatId: ObjectId): Chat? = chats.findOne(Chat::id eq chatId)

    override suspend fun insertChat(chat: Chat): Boolean = chats.insertOne(chat).wasAcknowledged()
}
