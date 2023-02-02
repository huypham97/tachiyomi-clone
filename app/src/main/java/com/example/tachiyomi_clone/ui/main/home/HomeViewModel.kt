package com.example.tachiyomi_clone.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.ui.base.BaseViewModel
import com.example.tachiyomi_clone.usecase.HomeUseCase
import com.example.tachiyomi_clone.usecase.NetworkToLocalUseCase
import com.example.tachiyomi_clone.utils.Logger
import com.example.tachiyomi_clone.utils.withIOContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase,
    private val networkToLocalUseCase: NetworkToLocalUseCase,
) : BaseViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
    }

    private val _popularManga: MutableLiveData<List<MangaEntity>> = MutableLiveData()
    val popularManga: LiveData<List<MangaEntity>>
        get() = _popularManga

    fun fetchPopularMangaPage(): Flow<PagingData<MangaEntity>> = Pager(
        PagingConfig(pageSize = 25),
    ) {
        homeUseCase.getPopularMangaPage()
    }.flow.map {
        it.map { manga ->
            withIOContext {
                networkToLocalUseCase.await(manga)
            }
        }
    }.cachedIn(viewModelScope)

    fun fetchPopularManga() {
        viewModelScope.launch {
            homeUseCase.getPopularManga()
                .onStart { showLoadingDialog() }
                .onCompletion { dismissLoadingDialog() }
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _popularManga.value = result.data.mangas?.take(5)
                        }
                        is Result.Error -> {
                            Logger.e(
                                TAG,
                                "[${TAG}] fetchPopularManga() --> error: ${result.exception}"
                            )
                        }
                    }
                }
        }
    }
}