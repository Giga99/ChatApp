package com.medium.client.domain.repositories

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun observeUsername(): Flow<String?>
}
