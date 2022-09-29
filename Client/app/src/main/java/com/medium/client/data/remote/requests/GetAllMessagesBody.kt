package com.medium.client.data.remote.requests

import kotlinx.serialization.Serializable

@Serializable
data class GetAllMessagesBody(
    val chatId: String
)
