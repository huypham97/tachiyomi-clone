package com.example.tachiyomi_clone.di.module

import androidx.lifecycle.ViewModel
import com.example.tachiyomi_clone.di.ViewModelKey
import com.example.tachiyomi_clone.ui.home.HomeViewModel
import com.example.tachiyomi_clone.ui.manga.MangaViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class HomeModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MangaViewModel::class)
    abstract fun bindMangaViewModel(viewModel: MangaViewModel): ViewModel
}