package com.medium.routes

import com.medium.data.mappers.toResponse
import com.medium.data.user.UserDataSource
import com.medium.utils.toBasicResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.koin.ktor.ext.inject

fun Route.usersRoutes() {
    val userDataSource by inject<UserDataSource>()

    route("users/") {
        searchUsers(
            userDataSource = userDataSource
        )
    }
}

fun Route.searchUsers(
    userDataSource: UserDataSource
) {
    authenticate {
        get("search") {
            val query = call.request.queryParameters.getOrFail("query")
            val users = userDataSource.searchUsersByUsername(query).map { it.toResponse() }

            val status = HttpStatusCode.OK
            call.respond(
                status = status,
                message = status.toBasicResponse(response = users)
            )
        }
    }
}
