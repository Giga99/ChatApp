package com.medium.client.data.remote.requests

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetAllMessagesBody(
    val chatId: String
)
