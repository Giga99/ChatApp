package com.medium.data.user

interface UserDataSource {

    suspend fun getUserByUsername(username: String): User?

    suspend fun insertUser(user: User): Boolean

    suspend fun searchUsersByUsername(username: String): List<User>
}
