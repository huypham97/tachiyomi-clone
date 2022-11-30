package com.example.tachiyomi_clone.di.module

import com.example.tachiyomi_clone.di.scope.ActivityScoped
import com.example.tachiyomi_clone.ui.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
abstract class AppActivityModule {
    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun bindHomeActivity(): HomeActivity
}