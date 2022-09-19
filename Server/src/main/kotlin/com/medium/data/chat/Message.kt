package com.medium.data.chat

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Message(
    @BsonId val id: ObjectId = ObjectId(),
    val text: String,
    val from: String,
    val to: String,
    val timestamp: Long
)
