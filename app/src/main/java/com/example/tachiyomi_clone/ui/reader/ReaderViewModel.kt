package com.example.tachiyomi_clone.ui.reader

import androidx.lifecycle.viewModelScope
import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.entity.PageEntity
import com.example.tachiyomi_clone.data.network.common.ConnectionHelper
import com.example.tachiyomi_clone.ui.base.BaseViewModel
import com.example.tachiyomi_clone.usecase.PageLoadUseCase
import com.example.tachiyomi_clone.utils.Logger
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReaderViewModel @Inject constructor(
    private val pageLoadUseCase: PageLoadUseCase,
    private val connectionHelper: ConnectionHelper
) :
    BaseViewModel(connectionHelper) {

    companion object {
        const val TAG = "ReaderViewModel"
    }

    var pageList: MutableList<PageEntity> = mutableListOf()

    fun getPages(chapterUrl: String) {
        pageList.clear()
        viewModelScope.launch {
            pageLoadUseCase.fetchPageList(chapterUrl)
                .catch { e ->
                    Logger.e(TAG, "[${TAG}] getPages() --> error: ${e.message}")
                }
                .onStart {
                    showLoadingDialog()
                }
                .onCompletion {
                    pageList.sortBy { it.index }
                    dismissLoadingDialog()
                    Logger.d(
                        TAG,
                        "[${TAG}] getPages() --> response success: $pageList"
                    )
                }
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            Logger.d(
                                TAG,
                                "[${TAG}] getPages() --> response next: ${result.data}"
                            )
                            pageList.add(result.data)
                        }
                        is Result.Error -> {
                            Logger.e(TAG, "[${TAG}] getPages() --> error: ${result.exception}")
                        }
                    }
                }
        }
    }

}