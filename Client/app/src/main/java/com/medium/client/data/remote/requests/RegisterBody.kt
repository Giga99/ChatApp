package com.medium.client.data.remote.requests

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterBody(
    val username: String,
    val email: String,
    val password: String
)
