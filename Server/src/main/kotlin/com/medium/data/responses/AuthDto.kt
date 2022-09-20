package com.medium.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthDto(
    val accessToken: String,
    val refreshToken: String
)
