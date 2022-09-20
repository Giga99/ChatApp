package com.medium.security.manager

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.interfaces.Payload
import com.medium.data.user.User
import com.medium.security.token.TokenClaim
import io.ktor.server.auth.jwt.*

interface TokensManager {

    fun generateVerifier(): JWTVerifier

    fun checkToken(credential: JWTCredential): Boolean

    fun generateAccessToken(user: User): String

    fun generateRefreshToken(user: User): String

    fun isTokenExpired(token: String): Boolean

    fun isTokenExpired(payload: Payload?): Boolean

    fun getUsernameFromToken(token: String): String
}
