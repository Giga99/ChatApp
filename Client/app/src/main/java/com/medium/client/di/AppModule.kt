package com.medium.client.di

import android.content.Context
import android.net.ConnectivityManager
import com.medium.client.R
import com.medium.client.common.annotations.BaseUrl
import com.medium.client.common.wrappers.connectivity.NetworkConnectivityManager
import com.medium.client.common.wrappers.connectivity.NetworkConnectivityManagerImpl
import com.medium.client.common.wrappers.session_manager.SessionManager
import com.medium.client.common.wrappers.session_manager.SessionManagerImpl
import com.medium.client.data.local.data_store.ChatAppDataStore
import com.medium.client.domain.repositories.AuthRepository
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
    @BaseUrl
    @Provides
    fun provideBaseUrl(
        @ApplicationContext context: Context
    ): String = context.getString(R.string.base_url)

    @Singleton
    @Provides
    fun provideSessionManager(
        chatAppDataStore: ChatAppDataStore,
        authRepository: AuthRepository
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
