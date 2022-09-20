package com.medium.plugins

import com.medium.security.manager.TokensManager
import com.medium.session.ChatSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val tokensManager by inject<TokensManager>()

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

    install(Sessions) {
        cookie<ChatSession>("chat_session")
    }
}
