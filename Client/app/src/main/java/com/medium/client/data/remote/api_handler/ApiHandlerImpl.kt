package com.medium.client.data.remote.api_handler

import com.medium.client.common.core.ApiResponse
import com.medium.client.common.core.BasicApiResponse
import com.medium.client.common.core.Result
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ApiHandlerImpl @Inject constructor(
    private val moshi: Moshi
) : ApiHandler {

    override suspend fun <T> handleCall(call: suspend () -> ApiResponse<T>): Result<T> =
        withContext(Dispatchers.IO) {
            try {
                val response = call()
                if (!response.isSuccessful) {
                    val data = convertErrorToBasicApiResponse(response = response)
                    return@withContext Result.Error(message = data?.message ?: "Unknown error!")
                }
                val apiResponse =
                    response.body() ?: return@withContext Result.Error("Unknown error!")
                if (!apiResponse.successful) {
                    return@withContext Result.Error(apiResponse.message)
                } else if (apiResponse.response == null) {
                    return@withContext Result.Error("Unknown error!")
                }
                Result.Success(apiResponse.response)
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(e.message ?: "Unknown error!")
            }
        }

    private fun <T> convertErrorToBasicApiResponse(
        response: ApiResponse<T>
    ): BasicApiResponse<Unit>? {
        val error = response.errorBody()
        return if (error != null) {
            val type = Types.newParameterizedType(
                BasicApiResponse::class.java,
                Unit::class.java
            )
            val adapter = moshi.adapter<BasicApiResponse<Unit>>(type)
            adapter.fromJson(error.source())
        } else null
    }
}
