package com.medium.security.manager

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import com.medium.data.user.User
import com.medium.security.token.JwtTokenService
import com.medium.security.token.TokenClaim
import com.medium.security.token.TokenConfig
import com.medium.security.token.TokenService
import io.ktor.server.auth.jwt.*
import java.util.*

class TokensManagerImpl(
    issuer: String,
    audience: String
) : TokensManager {

    private val tokenService: TokenService

    private val accessTokenConfig: TokenConfig
    private val refreshTokenConfig: TokenConfig

    init {
        tokenService = JwtTokenService()


        accessTokenConfig = TokenConfig(
            issuer = issuer,
            audience = audience,
            expiresIn = ACCESS_TOKEN_DURATION,
            secret = System.getenv("ACCESS_TOKEN_SECRET")
        )

        refreshTokenConfig = TokenConfig(
            issuer = issuer,
            audience = audience,
            expiresIn = REFRESH_TOKEN_DURATION,
            secret = System.getenv("REFRESH_TOKEN_SECRET")
        )
    }

    override fun generateVerifier(): JWTVerifier =
        JWT
            .require(Algorithm.HMAC256(accessTokenConfig.secret))
            .withAudience(accessTokenConfig.audience)
            .withIssuer(accessTokenConfig.issuer)
            .build()

    override fun checkToken(credential: JWTCredential): Boolean =
        credential.payload.audience.contains(accessTokenConfig.audience)
                && credential.payload.issuer == accessTokenConfig.issuer

    override fun generateAccessToken(user: User): String = tokenService.generate(
        config = accessTokenConfig,
        TokenClaim(
            name = "username",
            value = user.username
        )
    )

    override fun generateRefreshToken(user: User): String = tokenService.generate(
        config = refreshTokenConfig,
        TokenClaim(
            name = "username",
            value = user.username
        )
    )

    override fun isTokenExpired(token: String): Boolean =
        tokenService.decodeToken(token).expiresAt.before(Date(System.currentTimeMillis()))

    override fun isTokenExpired(payload: Payload?): Boolean =
        payload?.expiresAt?.before(Date(System.currentTimeMillis())) == true

    override fun getUsernameFromToken(token: String): String =
        tokenService.decodeToken(token).getClaim("username").asString()

    private companion object {
        const val ACCESS_TOKEN_DURATION = 1000L * 60L * 15L
        const val REFRESH_TOKEN_DURATION = 1000L * 60L * 60L * 24L * 30L
    }
}
