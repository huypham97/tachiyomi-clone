package com.example.tachiyomi_clone.ui.manga.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.network.common.ConnectionHelper
import com.example.tachiyomi_clone.ui.base.BaseViewModel
import com.example.tachiyomi_clone.usecase.GetMangaWithChaptersUseCase
import com.example.tachiyomi_clone.usecase.NetworkToLocalUseCase
import com.example.tachiyomi_clone.usecase.UpdateMangaUseCase
import com.example.tachiyomi_clone.utils.Logger
import com.example.tachiyomi_clone.utils.withIOContext
import com.example.tachiyomi_clone.utils.withUIContext
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class MangaDetailViewModel @Inject constructor(
    private val getMangaWithChaptersUseCase: GetMangaWithChaptersUseCase,
    private val updateMangaUseCase: UpdateMangaUseCase,
    private val networkToLocalUseCase: NetworkToLocalUseCase,
    private val connectionHelper: ConnectionHelper
) :
    BaseViewModel(connectionHelper) {

    private val _manga: MutableLiveData<MangaEntity> = MutableLiveData()
    val manga: LiveData<MangaEntity>
        get() = _manga

    private val _chapters: MutableLiveData<List<ChapterEntity>> = MutableLiveData()
    val chapters: LiveData<List<ChapterEntity>>
        get() = _chapters

    var selectedChapter: ChapterEntity? = null

    var ascendingSort: Boolean = true

    var isFavorite: Boolean = false

    companion object {
        private const val TAG = "MangaViewModel"
    }

    private suspend fun insertDetailMangaToLocal(manga: MangaEntity): MangaEntity {
        return networkToLocalUseCase.await(manga)
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

    fun fetchMangaFromSource(manga: MangaEntity) {
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
                            withIOContext {
                                val mangaLocal =
                                    insertDetailMangaToLocal(
                                        result.data.first.copy(
                                            url = manga.url,
                                            title = manga.title
                                        )
                                    )
                                withUIContext {
                                    isFavorite = mangaLocal.favorite
                                    _manga.value = mangaLocal
                                    Logger.d(
                                        TAG,
                                        "[${TAG}] fetchMangaFromLocalSource() --> response success: ${mangaLocal}"
                                    )
                                }
                            }
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

    fun setFavoriteManga() {
        viewModelScope.launch {
            isFavorite = !isFavorite
            _manga.value?.let {
                updateMangaUseCase.awaitUpdateFromSource(
                    it.copy(favorite = isFavorite)
                )
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

    fun sortChapterList() {
        ascendingSort = !ascendingSort
        _chapters.value =
            if (ascendingSort) _chapters.value?.sortedBy { it.sourceOrder } else _chapters.value?.sortedByDescending { it.sourceOrder }
    }
}