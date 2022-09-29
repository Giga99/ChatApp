package com.medium.client.data.remote.api_handler

import com.medium.client.common.core.BasicApiResponse
import com.medium.client.common.core.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

class ApiHandlerImpl @Inject constructor() : ApiHandler {

    override suspend fun <T> handleCall(call: suspend () -> BasicApiResponse<T>): Result<T> =
        withContext(Dispatchers.IO) {
            try {
                val response = call()
                println("RESPONSE: $response")
                if (!response.successful) {
                    return@withContext Result.Error(message = response.message)
                }
                val apiResponse =
                    response.response ?: return@withContext Result.Error("Unknown error!")
                Result.Success(apiResponse)
            } catch (e: Exception) {
                Timber.e(e)
                e.message?.let { message ->
                    val json = extractJsonFromError(message)
                    val response = Json.decodeFromString<BasicApiResponse<Unit>>(json)
                    Result.Error(response.message)
                } ?: Result.Error("Unknown error!")
            }
        }

    private fun extractJsonFromError(errorMessage: String): String {
        val jsonBegin = errorMessage.indexOfFirst { it == '"' }
        val jsonEnd = errorMessage.indexOfLast { it == '"' }
        return errorMessage.substring(jsonBegin + 1, jsonEnd)
    }
}
