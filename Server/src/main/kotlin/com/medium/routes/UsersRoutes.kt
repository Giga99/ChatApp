package com.medium.routes

import com.medium.user.UsersController
import com.medium.utils.toBasicResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.usersRoutes() {
    val usersController by inject<UsersController>()

    route("users/") {
        authenticate {
            searchUsers(usersController = usersController)
        }
    }
}

fun Route.searchUsers(
    usersController: UsersController
) {
    get("search") {
        val query = call.request.queryParameters["query"] ?: kotlin.run {
            val status = HttpStatusCode.BadRequest
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>(message = "`query` parameter is missing")
            )
            return@get
        }

        val users = usersController.searchUsers(query)
        val status = HttpStatusCode.OK
        call.respond(
            status = status,
            message = status.toBasicResponse(response = users)
        )
    }

}
