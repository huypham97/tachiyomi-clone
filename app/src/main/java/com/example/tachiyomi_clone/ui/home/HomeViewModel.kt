package com.example.tachiyomi_clone.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.ui.base.BaseViewModel
import com.example.tachiyomi_clone.usecase.home.HomeUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase
) : BaseViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
    }

    val listManga: Flow<PagingData<MangaEntity>> = Pager(
        PagingConfig(pageSize = 25),
    ) {
        homeUseCase.subscribe()
    }.flow.cachedIn(viewModelScope)

}