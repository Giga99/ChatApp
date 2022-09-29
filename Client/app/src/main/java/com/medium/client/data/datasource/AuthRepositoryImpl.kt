package com.medium.client.data.datasource

import com.medium.client.common.core.Result
import com.medium.client.data.local.data_store.ChatAppDataStore
import com.medium.client.data.remote.api_handler.ApiHandler
import com.medium.client.data.remote.requests.RefreshTokenBody
import com.medium.client.data.remote.responses.AuthResponse
import com.medium.client.data.remote.services.auth.AuthService
import com.medium.client.domain.mappers.requests_mappers.toBody
import com.medium.client.domain.models.requests.LoginRequest
import com.medium.client.domain.models.requests.RegisterRequest
import com.medium.client.domain.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val chatAppDataStore: ChatAppDataStore,
    private val apiHandler: ApiHandler
) : AuthRepository {

    override suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        val response =
            apiHandler.handleCall { authService.register(registerRequest.toBody()) }

        return response.data?.let { authResponse ->
            chatAppDataStore.setTokens(authResponse)
            Result.Success(Unit)
        } ?: Result.Error(response.message ?: "")
    }

    override suspend fun login(loginRequest: LoginRequest): Result<Unit> =
        withContext(Dispatchers.IO) {
            val response = apiHandler.handleCall { authService.login(loginRequest.toBody()) }

            response.data?.let { authResponse ->
                chatAppDataStore.setTokens(authResponse)
                Result.Success(Unit)
            } ?: Result.Error(response.message ?: "")
        }

    override suspend fun refreshToken(refreshToken: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            val response =
                apiHandler.handleCall { authService.refreshToken(RefreshTokenBody(refreshToken)) }

            response.data?.let { authResponse ->
                chatAppDataStore.setTokens(authResponse)
                Result.Success(Unit)
            } ?: Result.Error(response.message ?: "")
        }
}
