package com.example.tachiyomi_clone.provider

import com.example.tachiyomi_clone.data.local.database.DatabaseHandler
import com.example.tachiyomi_clone.data.local.database.mapper.chapterMapper
import com.example.tachiyomi_clone.data.model.dto.ChapterDto
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.network.http.MangaSource
import com.example.tachiyomi_clone.data.network.http.await
import com.example.tachiyomi_clone.data.repository.ChapterRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import java.util.*
import javax.inject.Inject

class ChapterProvider @Inject constructor(
    private val handler: DatabaseHandler,
    private val client: OkHttpClient,
    private val source: MangaSource,
) : ChapterRepository {

    override suspend fun getChapterByMangaId(mangaId: Long): List<ChapterEntity> {
        return handler.awaitList { chaptersQueries.getChaptersByMangaId(mangaId, chapterMapper) }
    }

    override suspend fun getChapterByMangaIdAsFlow(mangaId: Long): Flow<List<ChapterEntity>> {
        return handler.subscribeToList {
            chaptersQueries.getChaptersByMangaId(
                mangaId,
                chapterMapper
            )
        }
    }

    override suspend fun fetchChaptersFromNetwork(manga: MangaEntity): List<ChapterEntity> {
        return client.newCall(source.chapterListRequest(manga.url)).await().let {
            val networkChapters = source.chapterListParse(it)
        }
    }

}