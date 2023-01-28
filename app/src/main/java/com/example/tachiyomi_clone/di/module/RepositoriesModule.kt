package com.example.tachiyomi_clone.di.module

import com.example.tachiyomi_clone.data.repository.ChapterRepository
import com.example.tachiyomi_clone.data.repository.HomeRepository
import com.example.tachiyomi_clone.data.repository.MangaRepository
import com.example.tachiyomi_clone.data.repository.PageRepository
import com.example.tachiyomi_clone.provider.ChapterProvider
import com.example.tachiyomi_clone.provider.HomeProvider
import com.example.tachiyomi_clone.provider.MangaProvider
import com.example.tachiyomi_clone.provider.PageProvider
import dagger.Binds
import dagger.Module

@Module
@Suppress("UNUSED")
abstract class RepositoriesModule {
    @Binds
    abstract fun bindHomeRepository(homeProvider: HomeProvider): HomeRepository

    @Binds
    abstract fun bindMangaRepository(mangaProvider: MangaProvider): MangaRepository

    @Binds
    abstract fun bindChapterRepository(chapterProvider: ChapterProvider): ChapterRepository

    @Binds
    abstract fun bindPageRepository(pageProvider: PageProvider): PageRepository
}