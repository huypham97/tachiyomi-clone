package com.example.tachiyomi_clone.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tachiyomi_clone.di.ViewModelFactory
import com.example.tachiyomi_clone.di.ViewModelKey
import com.example.tachiyomi_clone.ui.main.MainViewModel
import com.example.tachiyomi_clone.ui.main.home.HomeViewModel
import com.example.tachiyomi_clone.ui.manga.MangaViewModel
import com.example.tachiyomi_clone.ui.reader.ReaderViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class AppViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MangaViewModel::class)
    abstract fun bindMangaViewModel(viewModel: MangaViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReaderViewModel::class)
    abstract fun bindReaderViewModel(viewModel: ReaderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel
}