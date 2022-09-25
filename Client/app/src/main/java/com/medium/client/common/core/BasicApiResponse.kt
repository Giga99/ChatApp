package com.medium.client.common.core

import com.squareup.moshi.JsonClass
import retrofit2.Response

@JsonClass(generateAdapter = true)
data class BasicApiResponse<T>(
    val successful: Boolean,
    val statusCode: Int,
    val message: String,
    val response: T? = null
)

typealias ApiResponse<T> = Response<BasicApiResponse<T>>
