package com.example.tachiyomi_clone.di.module

import androidx.lifecycle.ViewModelProvider
import com.example.tachiyomi_clone.di.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
@Suppress("UNUSED")
abstract class AppViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}