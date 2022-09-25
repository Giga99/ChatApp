package com.medium.client.data.remote.api_handler

import com.medium.client.common.core.ApiResponse
import com.medium.client.common.core.Result

interface ApiHandler {

    suspend fun <T> handleCall(call: suspend () -> ApiResponse<T>): Result<T>
}
