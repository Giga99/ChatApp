package com.medium.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val username: String,
)
