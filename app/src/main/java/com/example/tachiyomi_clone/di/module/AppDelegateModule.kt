package com.example.tachiyomi_clone.di.module

import android.content.Context
import android.content.SharedPreferences
import com.example.tachiyomi_clone.utils.Constant
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppDelegateModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(Constant.SHARED_PREFS_FILENAME, Context.MODE_PRIVATE)

}