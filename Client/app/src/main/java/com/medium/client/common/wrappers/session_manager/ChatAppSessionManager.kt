package com.medium.client.common.wrappers.session_manager

import com.medium.client.data.local.data_store.ChatAppDataStore
import com.medium.client.data.local.data_store.DataStoreKeys
import com.medium.client.domain.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatAppSessionManager @Inject constructor(
    private val chatAppDataStore: ChatAppDataStore,
    private val authRepository: AuthRepository
) {

    fun getAccessToken(): String? = runBlocking {
        chatAppDataStore.observeString(DataStoreKeys.ACCESS_TOKEN).first()
    }

    fun getRefreshToken(): String? = runBlocking {
        chatAppDataStore.observeString(DataStoreKeys.REFRESH_TOKEN).first()
    }

    fun logout() = runBlocking {
        chatAppDataStore.removeTokens()
    }

    fun refreshToken(refreshToken: String): String? = runBlocking {
        authRepository.refreshToken(refreshToken)
        getAccessToken()
    }

    suspend fun observeSessionStatus(): Flow<SessionStatus> = withContext(Dispatchers.IO) {
        chatAppDataStore.observeString(DataStoreKeys.ACCESS_TOKEN)
            .map {
                if (it != null) SessionStatus.LoggedIn else SessionStatus.LoggedOut
            }
            .distinctUntilChanged()
    }

    enum class SessionStatus {
        LoggedIn, LoggedOut
    }
}
