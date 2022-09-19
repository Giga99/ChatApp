package com.medium.plugins

import com.medium.routes.authRoutes
import com.medium.routes.usersRoutes
import com.medium.utils.toBasicResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            val status = HttpStatusCode.OK
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>(message = "Hello World!")
            )
        }

        authenticate {
            get("authenticate") {
                val status = HttpStatusCode.OK
                call.respond(
                    status = status,
                    message = status.toBasicResponse<Unit>(message = "Hello World!")
                )
            }
        }

        authRoutes()
        usersRoutes()
    }
}
