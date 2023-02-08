package com.example.tachiyomi_clone.ui.main.search

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.ui.base.BaseViewModel
import com.example.tachiyomi_clone.usecase.HomeUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase
) : BaseViewModel() {

    fun fetchSearchManga(query: String): Flow<PagingData<MangaEntity>> = Pager(
        PagingConfig(pageSize = 25),
    ) {
        homeUseCase.getSearchManga(query = query)
    }.flow.cachedIn(viewModelScope)

}