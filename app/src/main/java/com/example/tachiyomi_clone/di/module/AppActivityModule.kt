package com.example.tachiyomi_clone.di.module

import com.example.tachiyomi_clone.di.scope.ActivityScoped
import com.example.tachiyomi_clone.ui.main.MainActivity
import com.example.tachiyomi_clone.ui.manga.MangaActivity
import com.example.tachiyomi_clone.ui.page.MangaPageActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
abstract class AppActivityModule {
    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun bindMangaActivity(): MangaActivity

    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun bindMangaPageActivity(): MangaPageActivity
}