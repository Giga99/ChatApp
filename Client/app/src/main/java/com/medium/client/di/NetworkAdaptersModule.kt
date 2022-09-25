package com.medium.client.di

import com.medium.client.data.remote.adapters.MoshiUnitAdapter
import com.squareup.moshi.JsonAdapter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface NetworkAdaptersModule {

    @Binds
    @IntoSet
    abstract fun bindMoshiUnitAdapter(adapter: MoshiUnitAdapter): JsonAdapter<*>
}
