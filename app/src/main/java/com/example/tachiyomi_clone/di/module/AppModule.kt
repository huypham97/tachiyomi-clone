package com.example.tachiyomi_clone.di.module

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

@Module(
    includes = [
        AppDelegateModule::class,
        AppViewModelModule::class,
        AppActivityModule::class,
        CoroutinesModule::class]
)
abstract class AppModule {

    @Binds
    abstract fun provideContext(application: Application): Context

}