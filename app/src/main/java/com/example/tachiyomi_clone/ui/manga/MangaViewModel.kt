package com.example.tachiyomi_clone.ui.manga

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.ui.base.BaseViewModel
import com.example.tachiyomi_clone.usecase.GetMangaWithChaptersUseCase
import com.example.tachiyomi_clone.usecase.UpdateMangaUseCase
import com.example.tachiyomi_clone.utils.Logger
import com.example.tachiyomi_clone.utils.withIOContext
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
        viewModelScope.launch {
            val manga = getMangaWithChaptersUseCase.awaitManga(mangaId)
            val chapters = getMangaWithChaptersUseCase.awaitChapters(mangaId)

            val needRefreshInfo = !manga.initialized
            val needRefreshChapter = chapters.isEmpty()

            _manga.value = manga
            _chapters.value = chapters

            if (viewModelScope.isActive) {
                val fetchFromSourceTasks = listOf(
                    async { if (needRefreshInfo) fetchMangaFromSource(manga) },
                    async { if (needRefreshChapter) fetchChaptersFromSource(manga) },
                )
                fetchFromSourceTasks.awaitAll()
            }

            _manga.value = manga
            _chapters.value = chapters
        }
    }

    private suspend fun fetchMangaFromSource(manga: MangaEntity, manualFetch: Boolean = false) {
        withIOContext {
            val networkManga = getMangaWithChaptersUseCase.fetchMangaDetails(manga.url)
            Logger.d(
                TAG,
                "[${TAG}] fetchMangaFromSource() --> response success: $manga"
            )
            if (updateMangaUseCase.awaitUpdateFromSource(manga, networkManga, manualFetch))
                _manga.value = networkManga
        }
    }

    private suspend fun fetchChaptersFromSource(manga: MangaEntity) {
        withIOContext {
            val chapters = getMangaWithChaptersUseCase.getChapterList(manga)
            Logger.d(
                TAG,
                "[${TAG}] fetchChaptersFromSource() --> response success: $chapters"
            )
        }
    }

}