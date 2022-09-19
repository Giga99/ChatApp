package com.medium.plugins

import com.medium.security.manager.TokensManager
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(tokensManager: TokensManager) {
    authentication {
        jwt {
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier(tokensManager.generateVerifier())
            validate { credential ->
                if (tokensManager.checkToken(credential) && !tokensManager.isTokenExpired(credential.payload))
                    JWTPrincipal(credential.payload)
                else null
            }
        }
    }
}
