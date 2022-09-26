package com.medium.client.domain.models.ui

import java.time.Instant

data class MessageModel(
    val id: String,
    val text: String,
    val sender: String,
    val receiver: String,
    val timestamp: Instant
)
