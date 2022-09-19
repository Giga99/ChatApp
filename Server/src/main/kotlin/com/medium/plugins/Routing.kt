package com.medium.plugins

import com.medium.routes.authRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        authenticate {
            get("authenticate") {
                call.respond(HttpStatusCode.OK, "Hello World!")
            }
        }

        authRoutes()
    }
}
