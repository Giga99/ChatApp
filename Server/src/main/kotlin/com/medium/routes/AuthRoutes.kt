package com.medium.routes

import com.medium.data.requests.AuthRequest
import com.medium.data.requests.RefreshTokenRequest
import com.medium.data.responses.AuthResponse
import com.medium.data.user.User
import com.medium.data.user.UserDataSource
import com.medium.security.hashing.HashingService
import com.medium.security.hashing.SaltedHash
import com.medium.security.manager.TokensManager
import com.medium.utils.toBasicResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authRoutes() {
    val tokensManager by inject<TokensManager>()
    val hashingService by inject<HashingService>()
    val userDataSource by inject<UserDataSource>()

    route("auth/") {
        register(
            tokensManager = tokensManager,
            hashingService = hashingService,
            userDataSource = userDataSource
        )
        login(
            tokensManager = tokensManager,
            hashingService = hashingService,
            userDataSource = userDataSource
        )
        refreshToken(
            tokensManager = tokensManager,
            userDataSource = userDataSource
        )
    }
}

fun Route.register(
    tokensManager: TokensManager,
    hashingService: HashingService,
    userDataSource: UserDataSource
) {
    post("register") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            val status = HttpStatusCode.BadRequest
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>()
            )
            return@post
        }

        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        if (areFieldsBlank) {
            val status = HttpStatusCode.BadRequest
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>(message = "Username and password can't be blank")
            )
            return@post
        }

        val isPasswordTooShort = request.password.length < 8
        if (isPasswordTooShort) {
            val status = HttpStatusCode.BadRequest
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>(message = "Password is too short")
            )
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(value = request.password)
        val user = User(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
        val wasAcknowledged = userDataSource.insertUser(user)
        if (!wasAcknowledged) {
            val status = HttpStatusCode.ExpectationFailed
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>()
            )
            return@post
        }

        val accessToken = tokensManager.generateAccessToken(user = user)
        val refreshToken = tokensManager.generateRefreshToken(user = user)

        val status = HttpStatusCode.OK
        call.respond(
            status = status,
            message = status.toBasicResponse(
                response = AuthResponse(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            )
        )
    }
}

fun Route.login(
    tokensManager: TokensManager,
    hashingService: HashingService,
    userDataSource: UserDataSource
) {
    post("login") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            val status = HttpStatusCode.BadRequest
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>()
            )
            return@post
        }

        val user = userDataSource.getUserByUsername(request.username)
        if (user == null) {
            val status = HttpStatusCode.BadRequest
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>(message = "Incorrect username or password")
            )
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (!isValidPassword) {
            val status = HttpStatusCode.BadRequest
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>(message = "Incorrect username or password")
            )
            return@post
        }

        val accessToken = tokensManager.generateAccessToken(user = user)
        val refreshToken = tokensManager.generateRefreshToken(user = user)

        val status = HttpStatusCode.OK
        call.respond(
            status = status,
            message = status.toBasicResponse(
                response = AuthResponse(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            )
        )
    }
}

fun Route.refreshToken(
    tokensManager: TokensManager,
    userDataSource: UserDataSource
) {
    post("refreshToken") {
        val request = call.receiveNullable<RefreshTokenRequest>() ?: kotlin.run {
            val status = HttpStatusCode.BadRequest
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>()
            )
            return@post
        }

        val isExpired = tokensManager.isTokenExpired(token = request.refreshToken)
        if (isExpired) {
            val status = HttpStatusCode.Unauthorized
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>()
            )
            return@post
        }

        val username = tokensManager.getUsernameFromToken(token = request.refreshToken)
        val user = userDataSource.getUserByUsername(username)
        if (user == null) {
            val status = HttpStatusCode.NotFound
            call.respond(
                status = status,
                message = status.toBasicResponse<Unit>(message = "User with given username not found")
            )
            return@post
        }

        val accessToken = tokensManager.generateAccessToken(user = user)
        val refreshToken = tokensManager.generateRefreshToken(user = user)

        val status = HttpStatusCode.OK
        call.respond(
            status = status,
            message = status.toBasicResponse(
                response = AuthResponse(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            )
        )
    }
}
