package com.medium.client.data.local.data_store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.medium.client.data.remote.responses.AuthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatAppDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ChatAppDataStore {

    override suspend fun putValue(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override suspend fun putValue(key: Preferences.Key<Boolean>, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override fun observeString(key: Preferences.Key<String>): Flow<String?> =
        dataStore.data.map { it[key] }

    override fun observeBoolean(key: Preferences.Key<Boolean>): Flow<Boolean> =
        dataStore.data.map { it[key] ?: true }

    override suspend fun setTokens(response: AuthResponse?) {
        if (response != null) {
            putValue(DataStoreKeys.ACCESS_TOKEN, response.accessToken)
            putValue(DataStoreKeys.REFRESH_TOKEN, response.refreshToken)
        }
    }

    override suspend fun removeTokens() {
        dataStore.edit { preferences ->
            preferences.remove(DataStoreKeys.ACCESS_TOKEN)
            preferences.remove(DataStoreKeys.REFRESH_TOKEN)
        }
    }
}
