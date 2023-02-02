package com.example.tachiyomi_clone.di.module

import com.example.tachiyomi_clone.ui.main.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
internal abstract class AppFragmentModule {

    @ContributesAndroidInjector
    abstract fun bindHomeFragment(): HomeFragment

}