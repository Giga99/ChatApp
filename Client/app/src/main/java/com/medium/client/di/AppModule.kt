package com.medium.client.di

import android.content.Context
import android.net.ConnectivityManager
import com.medium.client.R
import com.medium.client.common.annotations.Host
import com.medium.client.common.annotations.Port
import com.medium.client.common.wrappers.connectivity.NetworkConnectivityManager
import com.medium.client.common.wrappers.connectivity.NetworkConnectivityManagerImpl
import com.medium.client.common.wrappers.session_manager.SessionManager
import com.medium.client.common.wrappers.session_manager.SessionManagerImpl
import com.medium.client.data.local.data_store.ChatAppDataStore
import com.medium.client.domain.repositories.AuthRepository
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Host
    @Provides
    fun provideHost(
        @ApplicationContext context: Context
    ): String = context.getString(R.string.host)

    @Singleton
    @Port
    @Provides
    fun provideBaseUrl(
        @ApplicationContext context: Context
    ): Int = context.getString(R.string.port).toInt()

//    @Singleton
//    @ChatWebSocketUrl
//    @Provides
//    fun provideChatWebSocketUrl(
//        @ApplicationContext context: Context
//    ): String = "${context.getString(R.string.base_url)}messaging/chat-socket"

    @Singleton
    @Provides
    fun provideSessionManager(
        chatAppDataStore: ChatAppDataStore,
        authRepository: Lazy<AuthRepository>
    ): SessionManager = SessionManagerImpl(
        chatAppDataStore = chatAppDataStore,
        authRepository = authRepository
    )

    @Singleton
    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    @Singleton
    @Provides
    fun provideNetworkConnectivityManager(
        connectivityManager: ConnectivityManager
    ): NetworkConnectivityManager = NetworkConnectivityManagerImpl(
        connectivityManager = connectivityManager
    )
}
