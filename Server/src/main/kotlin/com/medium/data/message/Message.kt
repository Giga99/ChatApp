package com.medium.data.message

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Message(
    @BsonId val id: ObjectId = ObjectId(),
    val text: String,
    val sender: String,
    val receiver: String,
    val timestamp: Long
)
