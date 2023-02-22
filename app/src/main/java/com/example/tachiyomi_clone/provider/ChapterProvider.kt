package com.example.tachiyomi_clone.provider

import com.example.tachiyomi_clone.data.local.database.DatabaseHandler
import com.example.tachiyomi_clone.data.local.database.mapper.chapterMapper
import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.dto.ChapterDto
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.model.mapper.ChapterMapper
import com.example.tachiyomi_clone.data.model.mapper.ErrorDataMapper
import com.example.tachiyomi_clone.data.network.service.HomeService
import com.example.tachiyomi_clone.data.repository.ChapterRepository
import com.example.tachiyomi_clone.di.qualifier.DefaultDispatcher
import com.example.tachiyomi_clone.utils.Logger
import com.example.tachiyomi_clone.utils.toLong
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChapterProvider @Inject constructor(
    private val chapterNetworkMapper: ChapterMapper,
    private val handler: DatabaseHandler,
    private val service: HomeService,
    errorDataMapper: ErrorDataMapper,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ChapterRepository, BaseProvider(defaultDispatcher, errorDataMapper) {

    companion object {
        const val TAG = "ChapterProvider"
    }

    override suspend fun getChapterByUrl(url: String): ChapterEntity? {
        return handler.awaitOneOrNull(true) {
            chaptersQueries.getChapterByUrl(chapterUrl = url, chapterMapper)
        }
    }

    override suspend fun getChaptersByMangaId(mangaId: Long): List<ChapterEntity> {
        return handler.awaitList { chaptersQueries.getChaptersByMangaId(mangaId, chapterMapper) }
    }

    override suspend fun getChapterByMangaIdAsFlow(mangaId: Long): Flow<List<ChapterEntity>> {
        return handler.subscribeToList {
            chaptersQueries.getChaptersByMangaId(
                mangaId, chapterMapper
            )
        }
    }

/*    override suspend fun fetchChaptersFromNetwork(manga: MangaEntity): Flow<Result<List<ChapterDto>>> {
        return flow {
            emit(safeApiCall {
                service.chapterListRequest(manga.url)
            })
        }.map { result ->
            when (result) {
                is Result.Success -> Result.Success(
                    ChapterDto.chapterListParse(result.data.body(), result.data.raw())
                )
                is Result.Error -> Result.Error(result.exception)
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }*/

    override suspend fun fetchChaptersFromNetwork(manga: MangaEntity): Flow<Result<List<ChapterEntity>>> {
        return flow {
            emit(safeApiCall {
                service.chapterListRequest(manga.url)
            })
        }.map { result ->
            when (result) {
                is Result.Success -> Result.Success(
                    ChapterDto.chapterListParse(result.data.body(), result.data.raw())
                        .mapIndexed { i, dto ->
                            chapterNetworkMapper.toEntity(dto)
                                .apply {
                                    name =
                                        dto.name?.let { cleanupChapterName(it, manga.title) } ?: ""
                                    url = dto.url ?: ""
                                    dateUpload = dto.date_upload
                                    mangaId = manga.id
                                    sourceOrder = i.toLong()
                                }
                        }.let {
                            return@let it.mapIndexed { index, chapterEntity ->
                                chapterEntity.apply {
                                    if (index - 1 in it.indices) nextChapterOrder =
                                        it[index - 1].sourceOrder
                                    if (index + 1 in it.indices) prevChapterOrder =
                                        it[index + 1].sourceOrder
                                }
                                chapterEntity
                            }
                        }
                )
                is Result.Error -> Result.Error(result.exception)
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }

    override suspend fun removeChaptersWithIds(chapterIds: List<Long>) {
        try {
            handler.await { chaptersQueries.removeChaptersWithIds(chapterIds) }
        } catch (e: Exception) {
            Logger.e(TAG, "[${TAG}] removeChaptersWithIds() --> error: ${e.message}")
        }
    }

    override suspend fun addAll(chapters: List<ChapterEntity>): List<ChapterEntity> {
        return try {
            handler.await(inTransaction = true) {
                chapters.map { chapter ->
                    chaptersQueries.insert(
                        chapter.mangaId,
                        chapter.url,
                        chapter.name,
                        chapter.read,
                        chapter.sourceOrder,
                        chapter.dateUpload,
                    )
                    val lastInsertId = chaptersQueries.selectLastInsertedRowId().executeAsOne()
                    chapter.copy(id = lastInsertId)
                }
            }
        } catch (e: Exception) {
            Logger.e(TAG, "[${TAG}] addAll() --> error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun addChapter(chapter: ChapterEntity) {
        try {
            handler.await(inTransaction = true) {
                chaptersQueries.insert(
                    chapter.mangaId,
                    chapter.url,
                    chapter.name,
                    chapter.read,
                    chapter.sourceOrder,
                    chapter.dateUpload,
                )
                val lastInsertId = chaptersQueries.selectLastInsertedRowId().executeAsOne()
                chapter.copy(id = lastInsertId)
            }
        } catch (e: Exception) {
            Logger.e(TAG, "[${TAG}] addAll() --> error: ${e.message}")
        }
    }

    override suspend fun updateAll(chapterUpdates: List<ChapterEntity>) {
        partialUpdate(*chapterUpdates.toTypedArray())
    }

    private suspend fun partialUpdate(vararg chapterUpdates: ChapterEntity) {
        handler.await(inTransaction = true) {
            chapterUpdates.forEach { chapterUpdate ->
                chaptersQueries.update(
                    mangaId = chapterUpdate.mangaId,
                    url = chapterUpdate.url,
                    name = chapterUpdate.name,
                    read = chapterUpdate.read.toLong(),
                    sourceOrder = chapterUpdate.sourceOrder,
                    dateUpload = chapterUpdate.dateUpload,
                    chapterId = chapterUpdate.id,
                )
            }
        }
    }

}