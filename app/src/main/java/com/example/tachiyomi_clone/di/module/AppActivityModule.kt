package com.example.tachiyomi_clone.di.module

import com.example.tachiyomi_clone.di.scope.ActivityScoped
import com.example.tachiyomi_clone.ui.home.HomeActivity
import com.example.tachiyomi_clone.ui.manga.MangaActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
abstract class AppActivityModule {
    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun bindHomeActivity(): HomeActivity

    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun bindMangaActivity(): MangaActivity
}