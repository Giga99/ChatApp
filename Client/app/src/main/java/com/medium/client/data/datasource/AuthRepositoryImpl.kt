package com.medium.client.data.datasource

import com.medium.client.common.core.Result
import com.medium.client.data.local.data_store.ChatAppDataStore
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
    private val chatAppDataStore: ChatAppDataStore
) : AuthRepository {

    override suspend fun register(registerRequest: RegisterRequest): Result<Unit> =
        withContext(Dispatchers.IO) {
            val response = authApiService.register(registerRequest.toBody())

            if (response.successful) {
                chatAppDataStore.setTokens(response.response)
                Result.Success(Unit)
            } else Result.Error(response.message)
        }

    override suspend fun login(loginRequest: LoginRequest): Result<Unit> =
        withContext(Dispatchers.IO) {
            val response = authApiService.login(loginRequest.toBody())

            if (response.successful) {
                chatAppDataStore.setTokens(response.response)
                Result.Success(Unit)
            } else Result.Error(response.message)
        }

    override suspend fun refreshToken(refreshToken: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            val response = authApiService.refreshToken(RefreshTokenBody(refreshToken))

            if (response.successful) {
                chatAppDataStore.setTokens(response.response)
                Result.Success(Unit)
            } else Result.Error(response.message)
        }
}
