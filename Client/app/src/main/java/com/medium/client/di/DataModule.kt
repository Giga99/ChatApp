package com.medium.client.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.medium.client.common.annotations.Host
import com.medium.client.common.annotations.Port
import com.medium.client.common.wrappers.session_manager.SessionManager
import com.medium.client.data.local.data_store.ChatAppDataStore
import com.medium.client.data.local.data_store.ChatAppDataStoreImpl
import com.medium.client.data.remote.api_handler.ApiHandler
import com.medium.client.data.remote.api_handler.ApiHandlerImpl
import com.medium.client.data.remote.services.auth.AuthService
import com.medium.client.data.remote.services.auth.AuthServiceImpl
import com.medium.client.data.remote.services.chats.ChatsService
import com.medium.client.data.remote.services.chats.ChatsServiceImpl
import com.medium.client.data.remote.services.users.UsersService
import com.medium.client.data.remote.services.users.UsersServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile(name = ChatAppDataStore.DATA_STORE_NAME)
    }

    @Singleton
    @Provides
    fun provideChatAppDataStore(
        dataStore: DataStore<Preferences>
    ): ChatAppDataStore = ChatAppDataStoreImpl(dataStore = dataStore)

    @Singleton
    @Provides
    fun provideHttpClient(
        @Host host: String,
        @Port port: Int,
        sessionManager: SessionManager
    ): HttpClient = HttpClient(CIO) {
//        expectSuccess = false
//        HttpResponseValidator {
//            validateResponse { response ->
//                println(response)
//            }
//        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(WebSockets) {
            pingInterval = 20_000
        }
//        install(JsonFeature) {
//            accept(ContentType.Application.Json)
//            serializer =
//                KotlinxSerializer()
//        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                }
            )
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = sessionManager.getAccessToken() ?: "",
                        refreshToken = sessionManager.getRefreshToken() ?: ""
                    )
                }
                refreshTokens {
                    val refreshToken = sessionManager.getRefreshToken() ?: ""
                    val accessToken = sessionManager.refreshToken(refreshToken) ?: ""
                    if (refreshToken.isBlank() || accessToken.isBlank()) sessionManager.logout()
                    BearerTokens(
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                }
            }
        }
        install(DefaultRequest) {
            url {
                this.host = host
                this.port = port
                this.protocol = URLProtocol.HTTP
            }
            contentType(ContentType.Application.Json)
        }
    }

    @Singleton
    @Provides
    fun provideAuthService(
        client: HttpClient
    ): AuthService = AuthServiceImpl(client = client)

    @Singleton
    @Provides
    fun provideUsersService(
        client: HttpClient
    ): UsersService = UsersServiceImpl(client = client)

    @Singleton
    @Provides
    fun provideChatsService(
        client: HttpClient
    ): ChatsService = ChatsServiceImpl(client = client)

    @Singleton
    @Provides
    fun provideApiHandler(): ApiHandler = ApiHandlerImpl()
}
