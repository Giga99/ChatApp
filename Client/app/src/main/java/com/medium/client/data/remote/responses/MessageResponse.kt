package com.medium.client.data.remote.responses

import com.squareup.moshi.JsonClass
import java.time.Instant

@JsonClass(generateAdapter = true)
data class MessageResponse(
    val id: String,
    val text: String,
    val sender: String,
    val receiver: String,
    val timestamp: Instant
)
