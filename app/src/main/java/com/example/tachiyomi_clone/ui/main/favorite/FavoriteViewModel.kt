package com.example.tachiyomi_clone.ui.main.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.ui.base.BaseViewModel
import com.example.tachiyomi_clone.usecase.NetworkToLocalUseCase
import com.example.tachiyomi_clone.usecase.UpdateMangaUseCase
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    private val networkToLocalUseCase: NetworkToLocalUseCase,
    private val updateMangaUseCase: UpdateMangaUseCase,
) : BaseViewModel() {

    private val _listFavorite: MutableLiveData<List<MangaEntity>> = MutableLiveData()
    val listFavorite: LiveData<List<MangaEntity>>
        get() = _listFavorite

    fun fetchFavoriteMangasFromLocal() {
        viewModelScope.launch {
            networkToLocalUseCase.getMangasFavorite().collect {
                _listFavorite.value = it
            }
        }
    }

    fun clearFavoriteMangas() {
        viewModelScope.launch {
            _listFavorite.value?.forEach {
                updateMangaUseCase.awaitUpdateFromSource(
                    it
                )
            }
            if (viewModelScope.isActive)
                fetchFavoriteMangasFromLocal()
        }
    }

}