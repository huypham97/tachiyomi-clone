package com.example.tachiyomi_clone.ui.main

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

class MainViewModel @Inject constructor() : BaseViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

}