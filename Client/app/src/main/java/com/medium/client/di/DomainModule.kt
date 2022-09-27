package com.medium.client.di

import com.medium.client.data.datasource.AuthRepositoryImpl
import com.medium.client.data.datasource.ChatsRepositoryImpl
import com.medium.client.data.datasource.DataStoreRepositoryImpl
import com.medium.client.data.datasource.UsersRepositoryImpl
import com.medium.client.domain.repositories.AuthRepository
import com.medium.client.domain.repositories.ChatsRepository
import com.medium.client.domain.repositories.DataStoreRepository
import com.medium.client.domain.repositories.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    @Singleton
    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    fun bindUsersRepository(usersRepositoryImpl: UsersRepositoryImpl): UsersRepository

    @Singleton
    @Binds
    fun bindChatsRepository(chatsRepositoryImpl: ChatsRepositoryImpl): ChatsRepository

    @Singleton
    @Binds
    fun bindDataStoreRepository(dataStoreRepositoryImpl: DataStoreRepositoryImpl): DataStoreRepository
}
