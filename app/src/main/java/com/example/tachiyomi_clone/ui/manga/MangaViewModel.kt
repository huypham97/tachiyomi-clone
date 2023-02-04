package com.example.tachiyomi_clone.ui.manga

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.ui.base.BaseViewModel
import com.example.tachiyomi_clone.usecase.GetMangaWithChaptersUseCase
import com.example.tachiyomi_clone.usecase.UpdateMangaUseCase
import com.example.tachiyomi_clone.utils.Logger
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class MangaViewModel @Inject constructor(
    private val getMangaWithChaptersUseCase: GetMangaWithChaptersUseCase,
    private val updateMangaUseCase: UpdateMangaUseCase
) :
    BaseViewModel() {

    private val _manga: MutableLiveData<MangaEntity> = MutableLiveData()
    val manga: LiveData<MangaEntity>
        get() = _manga

    private val _chapters: MutableLiveData<List<ChapterEntity>> = MutableLiveData()
    val chapters: LiveData<List<ChapterEntity>>
        get() = _chapters

    var ascendingSort: MutableLiveData<Boolean> = MutableLiveData(false)

    companion object {
        private const val TAG = "MangaViewModel"
    }

/*    fun fetchDetailMangaFromLocal(mangaId: Long) {
        viewModelScope.launch {
            getMangaWithChaptersUseCase.subscribe(mangaId).distinctUntilChanged()
                .collectLatest { (manga, chapters) ->
                    Logger.d(
                        TAG,
                        "[${TAG}] fetchDetailMangaFromLocal() --> response success: $manga $chapters"
                    )
                }
        }
    }

    fun fetchDetailManga(mangaId: Long) {
        showLoadingDialog()
        viewModelScope.launch {
            val manga = async { getMangaWithChaptersUseCase.awaitManga(mangaId) }
            val chapters = async { getMangaWithChaptersUseCase.awaitChapters(mangaId) }

            var needRefreshInfo: Boolean
            var needRefreshChapter: Boolean
            manga.await().let {
                needRefreshInfo = !it.initialized
                launch(Dispatchers.Main) {
                    _manga.value = it
                }
            }
            chapters.await().let {
                needRefreshChapter = it.isEmpty()
                launch(Dispatchers.Main) {
                    _chapters.value = it
                }
            }

            if (viewModelScope.isActive) {
                if (needRefreshInfo or needRefreshChapter)
                    fetchMangaFromSource(manga.await())
                else dismissLoadingDialog()
            }
        }
    }*/

    fun fetchMangaFromSource(manga: MangaEntity, manualFetch: Boolean = false) {
        viewModelScope.launch {
            getMangaWithChaptersUseCase.fetchMangaDetails(manga)
                .onStart { showLoadingDialog() }
                .onCompletion { dismissLoadingDialog() }
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            /* if (withIOContext {
                                     updateMangaUseCase.awaitUpdateFromSource(
                                         manga,
                                         result.data.first,
                                         manualFetch
                                     )
                                 }
                             ) {
                                 _manga.value = result.data.first
                                 _chapters.value = result.data.second
                                 Logger.d(
                                     TAG,
                                     "[${TAG}] fetchMangaFromSource() --> response success: ${result.data}"
                                 )
                             }*/
                            _manga.value = result.data.first
                            _chapters.value = result.data.second
                            Logger.d(
                                TAG,
                                "[${TAG}] fetchMangaFromSource() --> response success: ${result.data}"
                            )
                        }
                        is Result.Error -> {
                            Logger.e(
                                TAG,
                                "[${TAG}] fetchMangaFromSource() --> error: ${result.exception}"
                            )
                        }
                    }
                }
        }
    }

/*    private suspend fun fetchChaptersFromSource(manga: MangaEntity) {
        getMangaWithChaptersUseCase.getChapterList(manga).collect { result ->
            when (result) {
                is Result.Success -> {
                    Logger.d(
                        TAG,
                        "[${TAG}] fetchChaptersFromSource() --> response success: ${result.data}"
                    )
                    withUIContext { _chapters.value = result.data }
                }
                is Result.Error -> {
                    Logger.e(
                        TAG,
                        "[${TAG}] fetchChaptersFromSource() --> error: ${result.exception}"
                    )
                }
            }
        }
    }*/
}