package com.medium.client.common.wrappers.session_manager

import com.medium.client.data.local.data_store.ChatAppDataStore
import javax.inject.Inject

class ChatAppSessionManager @Inject constructor(
    private val chatAppDataStore: ChatAppDataStore
) {

    fun getAccessToken(): String? = null

    fun getRefreshToken(): String? = null

    fun logout() = Unit

    fun refreshToken(refreshToken: String): String? = null
}
