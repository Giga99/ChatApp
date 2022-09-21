package com.medium.client.data.local.data_store

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface ChatAppDataStore {

    suspend fun putValue(key: Preferences.Key<String>, value: String?)

    suspend fun putValue(key: Preferences.Key<Boolean>, value: Boolean)

    fun observeString(key: Preferences.Key<String>): Flow<String?>

    fun observeBoolean(key: Preferences.Key<Boolean>): Flow<Boolean>

    companion object {
        const val DATA_STORE_NAME = "chatAppDataStore"
    }
}
