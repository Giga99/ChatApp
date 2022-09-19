package com.medium.plugins

import com.medium.data.user.UserDataSource
import com.medium.routes.authRoutes
import com.medium.security.hashing.HashingService
import com.medium.security.manager.TokensManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    tokensManager: TokensManager,
    hashingService: HashingService,
    userDataSource: UserDataSource
) {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        authenticate {
            get("authenticate") {
                call.respond(HttpStatusCode.OK, "Hello World!")
            }
        }

        authRoutes(
            tokensManager = tokensManager,
            hashingService = hashingService,
            userDataSource = userDataSource
        )
    }
}
