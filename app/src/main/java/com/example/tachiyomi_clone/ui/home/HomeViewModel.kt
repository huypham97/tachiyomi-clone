package com.example.tachiyomi_clone.ui.home

import androidx.lifecycle.viewModelScope
import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.ui.base.BaseViewModel
import com.example.tachiyomi_clone.usecase.home.HomeUseCase
import com.example.tachiyomi_clone.utils.Logger
import com.example.tachiyomi_clone.utils.asJsoup
import kotlinx.coroutines.launch
import okhttp3.Response
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase
) : BaseViewModel() {

    companion object {
        const val TAG = "HomeViewModel"
    }

    fun getPopularManga() {
        viewModelScope.launch {
            homeUseCase(page = 1)
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            Logger.d(
                                TAG,
                                "[${TAG}] getPopularManga() --> homeUseCase: ${(result.data as Response).asJsoup()}"
                            )
                        }
                        is Result.Error -> {
                            Logger.e(
                                TAG,
                                "[${TAG} getPopularManga() --> homeUseCase error: ${result.exception}]"
                            )
                        }
                    }
                }
        }
    }

}