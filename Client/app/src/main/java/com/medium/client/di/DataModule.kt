package com.medium.client.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.medium.client.common.annotations.BaseUrl
import com.medium.client.common.wrappers.session_manager.ChatAppSessionManager
import com.medium.client.data.local.data_store.ChatAppDataStore
import com.medium.client.data.local.data_store.ChatAppDataStoreImpl
import com.medium.client.data.remote.interceptors.AuthInterceptorImpl
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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
    fun provideMoshi(
//        adapters: Set<@JvmSuppressWildcards JsonAdapter<*>>
    ): Moshi = Moshi.Builder()
//        .apply { adapters.forEach { adapter -> add(adapter) } }
        .build()

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(
        moshi: Moshi
    ): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Singleton
    @Provides
    fun provideAuthInterceptorImpl(
        chatAppSessionManager: ChatAppSessionManager
    ): AuthInterceptorImpl = AuthInterceptorImpl(chatAppSessionManager = chatAppSessionManager)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptorImpl: AuthInterceptorImpl
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .addInterceptor(authInterceptorImpl)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        @BaseUrl apiUrl: String,
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(apiUrl)
        .client(okHttpClient)
        .addConverterFactory(moshiConverterFactory)
        .build()
}
