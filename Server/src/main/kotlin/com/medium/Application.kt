package com.medium

import com.medium.data.user.MongoUserDataSource
import com.medium.plugins.*
import com.medium.security.hashing.SHA256HashingService
import com.medium.security.token.JwtTokenService
import com.medium.security.token.TokenConfig
import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val mongoUsername = System.getenv("MONGO_USERNAME")
    val mongoPassword = System.getenv("MONGO_PASSWORD")
    val dbName = "chat-app"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://$mongoUsername:$mongoPassword@cluster0.kpks9jj.mongodb.net/$dbName?retryWrites=true&w=majority"
    ).coroutine.getDatabase(dbName)
    val userDataSource = MongoUserDataSource(db)
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365 * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    configureHTTP()
    configureSecurity(tokenConfig)
    configureMonitoring()
    configureSerialization()
    configureSockets()
    configureRouting(
        hashingService = hashingService,
        userDataSource = userDataSource,
        tokenService = tokenService,
        tokenConfig = tokenConfig
    )
}
