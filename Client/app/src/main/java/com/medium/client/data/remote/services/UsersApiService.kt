package com.medium.client.data.remote.services

import com.medium.client.common.core.ApiResponse
import com.medium.client.data.remote.responses.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersApiService {

    @GET("users/userDetails")
    suspend fun getUserDetails(): ApiResponse<UserResponse>

    @GET("users/search")
    suspend fun searchUsers(
        @Query(value = "query") query: String
    ): ApiResponse<List<UserResponse>>
}
