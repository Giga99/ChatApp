package com.medium.data.message

import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.or

class MongoMessageDataSource(
    db: CoroutineDatabase
) : MessageDataSource {

    private val messages = db.getCollection<Message>()

    override suspend fun getAllMessages(user1: String, user2: String): List<Message> = messages.find(
        or(
            and(Message::sender eq user1, Message::receiver eq user2),
            and(Message::sender eq user2, Message::receiver eq user1)
        )
    ).descendingSort(Message::timestamp).toList()

    override suspend fun getLastMessage(user1: String, user2: String): Message = messages.find(
        or(
            and(Message::sender eq user1, Message::receiver eq user2),
            and(Message::sender eq user2, Message::receiver eq user1)
        )
    ).descendingSort(Message::timestamp).toList().first()

    override suspend fun insertMessage(message: Message): Boolean = messages.insertOne(message).wasAcknowledged()
}
