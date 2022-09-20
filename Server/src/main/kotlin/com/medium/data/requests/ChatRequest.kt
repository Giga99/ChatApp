package com.medium.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val participant: String
)
