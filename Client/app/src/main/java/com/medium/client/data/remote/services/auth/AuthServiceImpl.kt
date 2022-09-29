package com.medium.client.data.remote.services.auth

import com.medium.client.common.core.BasicApiResponse
import com.medium.client.data.remote.requests.LoginBody
import com.medium.client.data.remote.requests.RefreshTokenBody
import com.medium.client.data.remote.requests.RegisterBody
import com.medium.client.data.remote.responses.AuthResponse
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val client: HttpClient
) : AuthService {

    override suspend fun register(body: RegisterBody): BasicApiResponse<AuthResponse> =
        client.post(path = AuthService.Endpoints.Register.url) {
            this.body = body
        }

    override suspend fun login(body: LoginBody): BasicApiResponse<AuthResponse> =
        client.post(path = AuthService.Endpoints.Login.url) {
            this.body = body
        }

    override suspend fun refreshToken(body: RefreshTokenBody): BasicApiResponse<AuthResponse> =
        client.post(path = AuthService.Endpoints.RefreshToken.url) {
            this.body = body
        }
}
