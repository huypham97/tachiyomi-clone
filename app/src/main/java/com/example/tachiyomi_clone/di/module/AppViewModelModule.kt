package com.example.tachiyomi_clone.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tachiyomi_clone.di.ViewModelFactory
import com.example.tachiyomi_clone.di.ViewModelKey
import com.example.tachiyomi_clone.ui.main.MainViewModel
import com.example.tachiyomi_clone.ui.main.favorite.FavoriteViewModel
import com.example.tachiyomi_clone.ui.main.home.HomeViewModel
import com.example.tachiyomi_clone.ui.main.search.SearchViewModel
import com.example.tachiyomi_clone.ui.manga.MangaViewModel
import com.example.tachiyomi_clone.ui.manga.detail.MangaDetailViewModel
import com.example.tachiyomi_clone.ui.page.MangaPageViewModel
import com.example.tachiyomi_clone.ui.page.detail.MangaPageDetailViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(MangaDetailViewModel::class)
    abstract fun bindMangaDetailViewModel(viewModel: MangaDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MangaPageViewModel::class)
    abstract fun bindMangaPageViewModel(viewModel: MangaPageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MangaPageDetailViewModel::class)
    abstract fun bindMangaPageDetailViewModel(viewModel: MangaPageDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    abstract fun bindFavoriteViewModel(viewModel: FavoriteViewModel): ViewModel
}