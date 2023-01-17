package com.example.tachiyomi_clone.provider

import com.example.tachiyomi_clone.data.local.database.DatabaseHandler
import com.example.tachiyomi_clone.data.local.database.mapper.chapterMapper
import com.example.tachiyomi_clone.data.model.dto.ChapterDto
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.network.http.MangaSource
import com.example.tachiyomi_clone.data.network.http.await
import com.example.tachiyomi_clone.data.repository.ChapterRepository
import com.example.tachiyomi_clone.utils.Logger
import com.example.tachiyomi_clone.utils.toLong
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

class ChapterProvider @Inject constructor(
    private val handler: DatabaseHandler,
    private val client: OkHttpClient,
    private val source: MangaSource,
) : ChapterRepository {

    companion object {
        const val TAG = "ChapterProvider"
    }

    override suspend fun getChapterByMangaId(mangaId: Long): List<ChapterEntity> {
        return handler.awaitList { chaptersQueries.getChaptersByMangaId(mangaId, chapterMapper) }
    }

    override suspend fun getChapterByMangaIdAsFlow(mangaId: Long): Flow<List<ChapterEntity>> {
        return handler.subscribeToList {
            chaptersQueries.getChaptersByMangaId(
                mangaId, chapterMapper
            )
        }
    }

    override suspend fun fetchChaptersFromNetwork(manga: MangaEntity): List<ChapterDto> {
        return client.newCall(source.chapterListRequest(manga.url)).await().let {
            source.chapterListParse(it)
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
                        chapter.scanlator,
                        chapter.read,
                        chapter.bookmark,
                        chapter.lastPageRead,
                        chapter.chapterNumber,
                        chapter.sourceOrder,
                        chapter.dateFetch,
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

    override suspend fun updateAll(chapterUpdates: List<ChapterDto>) {
        partialUpdate(*chapterUpdates.toTypedArray())
    }

    private suspend fun partialUpdate(vararg chapterUpdates: ChapterDto) {
        handler.await(inTransaction = true) {
            chapterUpdates.forEach { chapterUpdate ->
                chaptersQueries.update(
                    mangaId = chapterUpdate.manga_id,
                    url = chapterUpdate.url,
                    name = chapterUpdate.name,
                    scanlator = chapterUpdate.scanlator,
                    read = chapterUpdate.read.toLong(),
                    bookmark = chapterUpdate.bookmark.toLong(),
                    lastPageRead = chapterUpdate.last_page_read.toLong(),
                    chapterNumber = chapterUpdate.chapter_number.toDouble(),
                    sourceOrder = chapterUpdate.source_order.toLong(),
                    dateFetch = chapterUpdate.date_fetch,
                    dateUpload = chapterUpdate.date_upload,
                    chapterId = chapterUpdate.id ?: -1L,
                )
            }
        }
    }

}