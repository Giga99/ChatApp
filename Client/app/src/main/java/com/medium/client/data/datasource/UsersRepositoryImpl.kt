package com.medium.client.data.datasource

import com.medium.client.common.core.Result
import com.medium.client.data.remote.api_handler.ApiHandler
import com.medium.client.data.remote.services.UsersApiService
import com.medium.client.domain.mappers.ui_mappers.toModel
import com.medium.client.domain.models.ui.UserModel
import com.medium.client.domain.repositories.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val usersApiService: UsersApiService,
    private val apiHandler: ApiHandler
) : UsersRepository {

    override suspend fun searchUsers(query: String): Result<List<UserModel>> =
        withContext(Dispatchers.IO) {
            val response = apiHandler.handleCall { usersApiService.searchUsers(query = query) }

            response.data?.let { usersResponse ->
                Result.Success(usersResponse.map { it.toModel() })
            } ?: Result.Error(response.message ?: "")
        }
}
