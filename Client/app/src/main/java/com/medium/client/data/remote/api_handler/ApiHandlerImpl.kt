package com.medium.client.data.remote.api_handler

import com.medium.client.common.core.BasicApiResponse
import com.medium.client.common.core.Result
import io.ktor.client.call.*
import io.ktor.client.statement.*
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
                if (!response.successful) {
                    return@withContext Result.Error(message = response.message)
                }
                val apiResponse =
                    response.response ?: return@withContext Result.Error("Unknown error!")
                Result.Success(apiResponse)
            } catch (e: Exception) {
                Timber.e(e)
                e.message?.let { errorMessage ->
                    println(errorMessage)
                    val json = extractJsonFromError(errorMessage)
                    val message = if (json == null) errorMessage
                    else Json.decodeFromString<BasicApiResponse<Unit>>(json).message
                    Result.Error(message)
                } ?: Result.Error("Unknown error!")
            }
        }

    override suspend fun <T> handleSocket(call: suspend () -> T): Result<T> =
        withContext(Dispatchers.IO) {
            try {
                val response = call()
                Result.Success(response)
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(e.message ?: "Unknown error!")
            }
        }

    private fun extractJsonFromError(errorMessage: String): String? {
        val jsonBegin = errorMessage.indexOfFirst { it == '"' }
        val jsonEnd = errorMessage.indexOfLast { it == '"' }
        if (jsonBegin == -1 || jsonEnd == -1) return null
        return errorMessage.substring(jsonBegin + 1, jsonEnd)
    }
}
