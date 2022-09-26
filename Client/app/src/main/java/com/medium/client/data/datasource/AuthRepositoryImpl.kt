package com.medium.client.data.datasource

import com.medium.client.common.core.Result
import com.medium.client.data.local.data_store.ChatAppDataStore
import com.medium.client.data.remote.api_handler.ApiHandlerImpl
import com.medium.client.data.remote.requests.RefreshTokenBody
import com.medium.client.data.remote.services.AuthApiService
import com.medium.client.domain.mappers.requests_mappers.toBody
import com.medium.client.domain.models.requests.LoginRequest
import com.medium.client.domain.models.requests.RegisterRequest
import com.medium.client.domain.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val chatAppDataStore: ChatAppDataStore,
    private val apiHandler: ApiHandlerImpl
) : AuthRepository {

    override suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        val response =
            apiHandler.handleCall { authApiService.register(registerRequest.toBody()) }

        return response.data?.let { authResponse ->
            chatAppDataStore.setTokens(authResponse)
            Result.Success(Unit)
        } ?: Result.Error(response.message ?: "")
    }

    override suspend fun login(loginRequest: LoginRequest): Result<Unit> =
        withContext(Dispatchers.IO) {
            val response = apiHandler.handleCall { authApiService.login(loginRequest.toBody()) }

            response.data?.let { authResponse ->
                chatAppDataStore.setTokens(authResponse)
                Result.Success(Unit)
            } ?: Result.Error(response.message ?: "")
        }

    override suspend fun refreshToken(refreshToken: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            val response =
                apiHandler.handleCall { authApiService.refreshToken(RefreshTokenBody(refreshToken)) }

            response.data?.let { authResponse ->
                chatAppDataStore.setTokens(authResponse)
                Result.Success(Unit)
            } ?: Result.Error(response.message ?: "")
        }
}
