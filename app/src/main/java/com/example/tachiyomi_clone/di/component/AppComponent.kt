package com.example.tachiyomi_clone.di.component

import android.app.Application
import com.example.tachiyomi_clone.TachiyomiApplication
import com.example.tachiyomi_clone.di.module.AppFragmentModule
import com.example.tachiyomi_clone.di.module.AppModule
import com.example.tachiyomi_clone.di.module.NetworkModule
import com.example.tachiyomi_clone.di.module.RepositoriesModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        RepositoriesModule::class,
        AppFragmentModule::class,
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun networkModule(networkModule: NetworkModule): Builder

        fun build(): AppComponent
    }

    fun inject(application: TachiyomiApplication)
}