package com.example.tachiyomi_clone.di.module

import com.example.tachiyomi_clone.data.repository.HomeRepository
import com.example.tachiyomi_clone.data.repository.MangaRepository
import com.example.tachiyomi_clone.provider.HomeProvider
import com.example.tachiyomi_clone.provider.MangaProvider
import dagger.Binds
import dagger.Module

@Module
@Suppress("UNUSED")
abstract class RepositoriesModule {
    @Binds
    abstract fun bindHomeRepository(homeProvider: HomeProvider): HomeRepository

    @Binds
    abstract fun bindMangaRepository(mangaProvider: MangaProvider): MangaRepository
}