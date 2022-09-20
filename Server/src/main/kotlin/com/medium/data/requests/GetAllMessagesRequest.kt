package com.medium.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class GetAllMessagesRequest(
    val participant: String
)
