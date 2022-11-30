package com.example.tachiyomi_clone.di.module

import com.example.tachiyomi_clone.data.network.repository.HomeRepository
import com.example.tachiyomi_clone.provider.HomeProvider
import dagger.Binds
import dagger.Module

@Module
@Suppress("UNUSED")
abstract class RepositoriesModule {
    @Binds
    abstract fun bindHomeRepository(homeProvider: HomeProvider): HomeRepository
}