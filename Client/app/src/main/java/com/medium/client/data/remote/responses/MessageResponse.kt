package com.medium.client.data.remote.responses

import com.medium.client.data.remote.serializers.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class MessageResponse(
    val id: String,
    val text: String,
    val sender: String,
    val receiver: String,
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant
)
