package com.medium

import com.medium.data.user.MongoUserDataSource
import com.medium.data.user.User
import com.medium.plugins.*
import io.ktor.server.application.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    configureHTTP()
    configureSecurity()
    configureMonitoring()
    configureSerialization()
    configureSockets()
    configureRouting()
}
