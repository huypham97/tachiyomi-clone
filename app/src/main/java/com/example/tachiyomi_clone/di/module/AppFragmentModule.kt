package com.example.tachiyomi_clone.di.module

import com.example.tachiyomi_clone.ui.main.home.HomeFragment
import com.example.tachiyomi_clone.ui.main.search.SearchFragment
import com.example.tachiyomi_clone.ui.manga.detail.MangaDetailFragment
import com.example.tachiyomi_clone.ui.page.detail.MangaPageDetailFragment
import com.example.tachiyomi_clone.ui.reader.ReaderFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
internal abstract class AppFragmentModule {

    @ContributesAndroidInjector
    abstract fun bindHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun bindMangaDetailFragment(): MangaDetailFragment

    @ContributesAndroidInjector
    abstract fun bindReaderFragment(): ReaderFragment

    @ContributesAndroidInjector
    abstract fun bindMangaPageDetailFragment(): MangaPageDetailFragment

    @ContributesAndroidInjector
    abstract fun bindSearchFragment(): SearchFragment

}