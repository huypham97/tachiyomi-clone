package com.example.tachiyomi_clone.ui.page.detail

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tachiyomi_clone.data.model.entity.MODULE_TYPE
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.network.common.ConnectionHelper
import com.example.tachiyomi_clone.ui.base.BaseViewModel
import com.example.tachiyomi_clone.usecase.HomeUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MangaPageDetailViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase,
    private val connectionHelper: ConnectionHelper
) : BaseViewModel(connectionHelper) {

    fun fetchMangaPage(type: MODULE_TYPE): Flow<PagingData<MangaEntity>> = Pager(
        PagingConfig(pageSize = 25),
    ) {
        homeUseCase.getMangaPage(type)
    }.flow.cachedIn(viewModelScope)

    fun fetchGenreManga(query: String): Flow<PagingData<MangaEntity>> = Pager(
        PagingConfig(pageSize = 25),
    ) {
        homeUseCase.getSearchByGenreManga(query = query)
    }.flow.cachedIn(viewModelScope)
}