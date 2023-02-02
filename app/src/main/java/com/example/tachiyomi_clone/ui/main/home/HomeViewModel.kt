package com.example.tachiyomi_clone.ui.main.home

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.ui.base.BaseViewModel
import com.example.tachiyomi_clone.usecase.HomeUseCase
import com.example.tachiyomi_clone.usecase.NetworkToLocalUseCase
import com.example.tachiyomi_clone.utils.withIOContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase,
    private val networkToLocalUseCase: NetworkToLocalUseCase,
) : BaseViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
    }

    fun listManga(query: String): Flow<PagingData<MangaEntity>> = Pager(
        PagingConfig(pageSize = 25),
    ) {
        homeUseCase.subscribe(query)
    }.flow.map {
        it.map { manga ->
            withIOContext {
                networkToLocalUseCase.await(manga)
            }
        }
    }.cachedIn(viewModelScope)

}