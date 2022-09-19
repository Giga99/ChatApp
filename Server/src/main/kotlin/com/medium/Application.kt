package com.medium

import com.medium.data.user.MongoUserDataSource
import com.medium.plugins.*
import com.medium.security.hashing.SHA256HashingService
import com.medium.security.manager.TokensManagerImpl
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
        connectionString = "mongodb+srv://$mongoUsername:$mongoPassword@cluster0.kpks9jj.mongodb.net/?retryWrites=true&w=majority"
    ).coroutine.getDatabase(dbName)
    val userDataSource = MongoUserDataSource(db)

    val tokensManager = TokensManagerImpl(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString()
    )
    val hashingService = SHA256HashingService()

    configureHTTP()
    configureSecurity(tokensManager)
    configureMonitoring()
    configureSerialization()
    configureSockets()
    configureRouting(
        tokensManager = tokensManager,
        hashingService = hashingService,
        userDataSource = userDataSource
    )
}
