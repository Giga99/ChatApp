package com.medium.client.di

import android.content.Context
import com.medium.client.R
import com.medium.client.common.annotations.BaseUrl
import com.medium.client.common.wrappers.session_manager.ChatAppSessionManager
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
    ): ChatAppSessionManager = ChatAppSessionManager(
        chatAppDataStore = chatAppDataStore,
        authRepository = authRepository
    )
}
