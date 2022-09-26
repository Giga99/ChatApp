package com.medium.client.domain.repositories

import com.medium.client.common.core.Result
import com.medium.client.domain.models.ui.UserModel

interface UsersRepository {

    suspend fun searchUsers(query: String): Result<List<UserModel>>
}
