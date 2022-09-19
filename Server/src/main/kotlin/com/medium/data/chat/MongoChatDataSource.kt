package com.medium.data.chat

import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.or

class MongoChatDataSource(
    db: CoroutineDatabase
) : ChatDataSource {

    private val messages = db.getCollection<Message>()

    override suspend fun getAllMessages(user1: String, user2: String): List<Message> = messages.find(
        or(
            and(Message::from eq user1, Message::to eq user2),
            and(Message::from eq user2, Message::to eq user1)
        )
    ).toList()

    override suspend fun insertMessage(message: Message): Boolean = messages.insertOne(message).wasAcknowledged()
}
