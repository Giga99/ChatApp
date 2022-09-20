package com.medium.data.user

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.regex

class MongoUserDataSource(
    db: CoroutineDatabase
) : UserDataSource {

    private val users = db.getCollection<User>()

    override suspend fun getUserByUsername(username: String): User? = users.findOne(User::username eq username)

    override suspend fun insertUser(user: User): Boolean = users.insertOne(user).wasAcknowledged()

    override suspend fun searchUsersByUsername(username: String): List<User> =
        users.find((User::username).regex(username, "i")).toList()
}
