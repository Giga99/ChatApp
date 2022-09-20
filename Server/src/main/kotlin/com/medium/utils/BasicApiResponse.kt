package com.medium.utils

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class BasicApiResponse<T>(
    val statusCode: Int,
    val message: String,
    val response: T? = null
)

fun <T> HttpStatusCode.toBasicResponse(message: String = description, response: T? = null): BasicApiResponse<T> =
    BasicApiResponse(
        statusCode = value,
        message = message,
        response = response
    )
