package com.medium.plugins

import com.medium.auth.AuthController
import com.medium.chat.ChatController
import com.medium.data.chat.ChatDataSource
import com.medium.data.chat.MongoChatDataSource
import com.medium.data.message.MessageDataSource
import com.medium.data.message.MongoMessageDataSource
import com.medium.data.user.MongoUserDataSource
import com.medium.data.user.UserDataSource
import com.medium.security.hashing.HashingService
import com.medium.security.hashing.SHA256HashingService
import com.medium.security.manager.TokensManager
import com.medium.security.manager.TokensManagerImpl
import com.medium.user.UsersController
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun Application.configureKoin() {
    val chatModule = module {
        single<TokensManager> {
            TokensManagerImpl(
                issuer = environment.config.property("jwt.issuer").getString(),
                audience = environment.config.property("jwt.audience").getString()
            )
        }

        single<HashingService> {
            SHA256HashingService()
        }

        single {
            val mongoUsername = System.getenv("MONGO_USERNAME")
            val mongoPassword = System.getenv("MONGO_PASSWORD")
            val dbName = "chat-app"
            KMongo.createClient(
                connectionString = "mongodb+srv://$mongoUsername:$mongoPassword@cluster0.kpks9jj.mongodb.net/?retryWrites=true&w=majority"
            ).coroutine.getDatabase(dbName)
        }

        single<UserDataSource> {
            MongoUserDataSource(get())
        }

        single<MessageDataSource> {
            MongoMessageDataSource(get())
        }

        single<ChatDataSource> {
            MongoChatDataSource(get())
        }

        single {
            AuthController(get(), get(), get())
        }

        single {
            UsersController(get())
        }

        single {
            ChatController(get(), get())
        }
    }

    install(Koin) {
        slf4jLogger()
        modules(chatModule)
    }
}
