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
import com.example.tachiyomi_clone.utils.withUIContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
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

    companion object {
        private const val TAG = "MangaViewModel"
    }

    fun fetchDetailMangaFromLocal(mangaId: Long) {
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
            val manga = getMangaWithChaptersUseCase.awaitManga(mangaId)
            val chapters = getMangaWithChaptersUseCase.awaitChapters(mangaId)

            val needRefreshInfo = !manga.initialized
            val needRefreshChapter = chapters.isEmpty()

            withUIContext {
                _manga.value = manga
                _chapters.value = chapters
            }

            if (viewModelScope.isActive) {
                val fetchFromSourceTasks = listOf(
                    async { if (needRefreshInfo) fetchMangaFromSource(manga) },
                    async { if (needRefreshChapter) fetchChaptersFromSource(manga) },
                )
                val result = fetchFromSourceTasks.awaitAll()
                if (result.isNotEmpty()) withUIContext { dismissLoadingDialog() }
            }
        }
    }

    private suspend fun fetchMangaFromSource(manga: MangaEntity, manualFetch: Boolean = false) {
        getMangaWithChaptersUseCase.fetchMangaDetails(manga.url).collect { result ->
            when (result) {
                is Result.Success -> {
                    if (updateMangaUseCase.awaitUpdateFromSource(
                            manga,
                            result.data,
                            manualFetch
                        )
                    )
                        withUIContext { _manga.value = result.data }
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

    private suspend fun fetchChaptersFromSource(manga: MangaEntity) {
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
    }

}