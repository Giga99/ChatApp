package com.medium.client.data.datasource

import com.medium.client.data.local.data_store.ChatAppDataStore
import com.medium.client.data.local.data_store.DataStoreKeys
import com.medium.client.domain.repositories.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val chatAppDataStore: ChatAppDataStore
) : DataStoreRepository {

    override suspend fun observeUsername(): Flow<String?> =
        chatAppDataStore.observeString(DataStoreKeys.USERNAME)
}
