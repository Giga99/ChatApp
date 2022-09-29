package com.medium.client.data.remote.services.users

import com.medium.client.common.core.BasicApiResponse
import com.medium.client.data.remote.responses.UserResponse
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject

class UsersServiceImpl @Inject constructor(
    private val client: HttpClient
) : UsersService {

    override suspend fun getUserDetails(): BasicApiResponse<UserResponse> =
        client.get(path = UsersService.Endpoints.UserDetails.url)

    override suspend fun searchUsers(query: String): BasicApiResponse<List<UserResponse>> =
        client.get(path = UsersService.Endpoints.SearchUsers.url) {
            url {
                parameters.append("query", query)
            }
        }
}
