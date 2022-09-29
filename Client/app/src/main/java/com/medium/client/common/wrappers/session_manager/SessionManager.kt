package com.medium.client.common.wrappers.session_manager

import com.medium.client.data.local.data_store.ChatAppDataStore
import com.medium.client.data.local.data_store.DataStoreKeys
import com.medium.client.domain.repositories.AuthRepository
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface SessionManager {

    fun getAccessToken(): String?

    fun getRefreshToken(): String?

    fun logout()

    fun refreshToken(refreshToken: String): String?

    suspend fun observeSessionStatus(): Flow<SessionStatus>
}

class SessionManagerImpl @Inject constructor(
    private val chatAppDataStore: ChatAppDataStore,
    private val authRepository: Lazy<AuthRepository>
) : SessionManager {

    override fun getAccessToken(): String? = runBlocking {
        chatAppDataStore.observeString(DataStoreKeys.ACCESS_TOKEN).first()
    }

    override fun getRefreshToken(): String? = runBlocking {
        chatAppDataStore.observeString(DataStoreKeys.REFRESH_TOKEN).first()
    }

    override fun logout() = runBlocking {
        chatAppDataStore.removeTokens()
    }

    override fun refreshToken(refreshToken: String): String? = runBlocking {
        authRepository.get().refreshToken(refreshToken)
        getAccessToken()
    }

    override suspend fun observeSessionStatus(): Flow<SessionStatus> = withContext(Dispatchers.IO) {
        chatAppDataStore.observeString(DataStoreKeys.ACCESS_TOKEN)
            .map {
                if (it != null) SessionStatus.LoggedIn else SessionStatus.LoggedOut
            }
            .distinctUntilChanged()
    }
}

enum class SessionStatus {
    LoggedIn, LoggedOut
}
