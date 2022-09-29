package com.medium.client.data.remote.services.users

import com.medium.client.common.core.BasicApiResponse
import com.medium.client.data.remote.responses.UserResponse

interface UsersService {

    suspend fun getUserDetails(): BasicApiResponse<UserResponse>

    suspend fun searchUsers(query: String): BasicApiResponse<List<UserResponse>>

    sealed class Endpoints(val url: String) {
        object UserDetails : Endpoints("users/userDetails")
        object SearchUsers : Endpoints("users/search")
    }
}
