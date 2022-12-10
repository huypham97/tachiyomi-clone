package com.example.tachiyomi_clone

import android.app.Application
import com.example.tachiyomi_clone.di.component.DaggerAppComponent
import com.example.tachiyomi_clone.di.module.NetworkModule
import com.example.tachiyomi_clone.utils.Constant
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class TachiyomiApplication: Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
            .builder()
            .application(this)
            .networkModule(NetworkModule(Constant.BASE_URL))
            .build()
            .inject(this)
    }
}