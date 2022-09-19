package com.medium.security.token

import com.auth0.jwt.interfaces.DecodedJWT

interface TokenService {

    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String

    fun decodeToken(token: String): DecodedJWT
}
