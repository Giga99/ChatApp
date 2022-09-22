package com.medium.client.data.remote.services

import com.medium.client.common.core.BasicApiResponse
import com.medium.client.data.remote.requests.LoginBody
import com.medium.client.data.remote.requests.RefreshTokenBody
import com.medium.client.data.remote.requests.RegisterBody
import com.medium.client.data.remote.responses.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginBody
    ): BasicApiResponse<AuthResponse>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterBody
    ): BasicApiResponse<AuthResponse>

    @POST("auth/refreshToken")
    suspend fun refreshToken(
        @Body request: RefreshTokenBody
    ): BasicApiResponse<AuthResponse>
}
