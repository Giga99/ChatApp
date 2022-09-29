package com.medium.client.data.datasource

import com.medium.client.common.core.Result
import com.medium.client.data.local.data_store.ChatAppDataStore
import com.medium.client.data.local.data_store.DataStoreKeys
import com.medium.client.data.remote.api_handler.ApiHandler
import com.medium.client.data.remote.services.users.UsersService
import com.medium.client.domain.mappers.ui_mappers.toModel
import com.medium.client.domain.models.ui.UserModel
import com.medium.client.domain.repositories.UsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val usersService: UsersService,
    private val apiHandler: ApiHandler,
    private val chatAppDataStore: ChatAppDataStore
) : UsersRepository {

    override suspend fun getUserDetails(): Result<UserModel> {
        val response = apiHandler.handleCall { usersService.getUserDetails() }

        return response.data?.let {
            chatAppDataStore.putValue(DataStoreKeys.USERNAME, it.username)
            Result.Success(it.toModel())
        } ?: Result.Error(response.message ?: "")
    }

    override suspend fun searchUsers(query: String): Result<List<UserModel>> {
        val response =
            apiHandler.handleCall { usersService.searchUsers(query = query) }

        return response.data?.let { usersResponse ->
            Result.Success(usersResponse.map { it.toModel() })
        } ?: Result.Error(response.message ?: "")
    }
}
