package com.medium.plugins

import com.medium.data.user.UserDataSource
import com.medium.routes.authRoutes
import com.medium.security.hashing.HashingService
import com.medium.security.token.TokenConfig
import com.medium.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        authRoutes(
            hashingService = hashingService,
            userDataSource = userDataSource,
            tokenService = tokenService,
            tokenConfig = tokenConfig
        )
    }
}
