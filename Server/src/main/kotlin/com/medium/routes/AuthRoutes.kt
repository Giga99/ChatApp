package com.medium.routes

import com.medium.auth.AuthController
import com.medium.auth.AuthException
import com.medium.data.requests.AuthRequest
import com.medium.data.requests.RefreshTokenRequest
import com.medium.utils.toBasicResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authRoutes() {
    val authController by inject<AuthController>()

    route("auth/") {
        register(authController = authController)
        login(authController = authController)
        refreshToken(authController = authController)
    }
}

fun Route.register(authController: AuthController) {
    post("register") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            val status = HttpStatusCode.BadRequest
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>()
            )
            return@post
        }

        try {
            val response = authController.registerUser(request)
            val status = HttpStatusCode.OK
            call.respond(
                status = status,
                message = status.toBasicResponse(response = response)
            )
        } catch (e: AuthException) {
            call.respond(
                status = e.status,
                message = e.status.toBasicResponse<Unit>(message = e.message)
            )
        }
    }
}

fun Route.login(authController: AuthController) {
    post("login") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            val status = HttpStatusCode.BadRequest
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>()
            )
            return@post
        }

        try {
            val response = authController.loginUser(request)
            val status = HttpStatusCode.OK
            call.respond(
                status = status,
                message = status.toBasicResponse(response = response)
            )
        } catch (e: AuthException) {
            call.respond(
                status = e.status,
                message = e.status.toBasicResponse<Unit>(message = e.message)
            )
        }
    }
}

fun Route.refreshToken(authController: AuthController) {
    post("refreshToken") {
        val request = call.receiveNullable<RefreshTokenRequest>() ?: kotlin.run {
            val status = HttpStatusCode.BadRequest
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>()
            )
            return@post
        }

        try {
            val response = authController.refreshToken(request)
            val status = HttpStatusCode.OK
            call.respond(
                status = status,
                message = status.toBasicResponse(response = response)
            )
        } catch (e: AuthException) {
            call.respond(
                status = e.status,
                message = e.status.toBasicResponse<Unit>(message = e.message)
            )
        }
    }
}
