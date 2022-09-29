package com.medium.client.common.core

import kotlinx.serialization.Serializable

@Serializable
data class BasicApiResponse<T>(
    val successful: Boolean,
    val statusCode: Int,
    val message: String,
    val response: T? = null
)
