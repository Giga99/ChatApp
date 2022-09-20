package com.medium.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.*

class JwtTokenService : TokenService {

    override fun generate(config: TokenConfig, vararg claims: TokenClaim): String =
        JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))
            .apply { claims.forEach { claim -> withClaim(claim.name, claim.value) } }
            .sign(Algorithm.HMAC256(config.secret))

    override fun decodeToken(token: String): DecodedJWT = JWT.decode(token)
}
