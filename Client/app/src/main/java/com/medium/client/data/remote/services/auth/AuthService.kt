package com.medium.client.data.remote.services.auth

import com.medium.client.common.core.BasicApiResponse
import com.medium.client.data.remote.requests.LoginBody
import com.medium.client.data.remote.requests.RefreshTokenBody
import com.medium.client.data.remote.requests.RegisterBody
import com.medium.client.data.remote.responses.AuthResponse

interface AuthService {

    suspend fun register(body: RegisterBody): BasicApiResponse<AuthResponse>

    suspend fun login(body: LoginBody): BasicApiResponse<AuthResponse>

    suspend fun refreshToken(body: RefreshTokenBody): BasicApiResponse<AuthResponse>

    sealed class Endpoints(val path: String) {
        object Register : Endpoints("auth/register")
        object Login : Endpoints("auth/login")
        object RefreshToken : Endpoints("auth/refreshToken")
    }
}
