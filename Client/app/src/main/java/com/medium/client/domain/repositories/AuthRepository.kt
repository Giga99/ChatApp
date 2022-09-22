package com.medium.client.domain.repositories

import com.medium.client.common.core.Result
import com.medium.client.domain.models.requests.LoginRequest
import com.medium.client.domain.models.requests.RegisterRequest

interface AuthRepository {

    suspend fun register(registerRequest: RegisterRequest): Result<Unit>

    suspend fun login(loginRequest: LoginRequest): Result<Unit>

    suspend fun refreshToken(refreshToken: String): Result<Unit>
}
