package com.medium.data.chat

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Chat(
    @BsonId val id: ObjectId = ObjectId(),
    val user1: String,
    val user2: String
)
