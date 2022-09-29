package com.medium.client.data.remote.api_handler

import com.medium.client.common.core.BasicApiResponse
import com.medium.client.common.core.Result

interface ApiHandler {

    suspend fun <T> handleCall(call: suspend () -> BasicApiResponse<T>): Result<T>

    suspend fun <T> handleSocket(call: suspend () -> T): Result<T>
}
