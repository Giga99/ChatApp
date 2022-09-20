package com.medium.user

import com.medium.data.mappers.toDto
import com.medium.data.responses.UserDto
import com.medium.data.user.UserDataSource

class UsersController(
    private val userDataSource: UserDataSource
) {

    suspend fun searchUsers(query: String): List<UserDto> =
        userDataSource.searchUsersByUsername(query).map { it.toDto() }
}
