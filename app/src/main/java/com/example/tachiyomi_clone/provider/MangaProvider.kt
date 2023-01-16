package com.example.tachiyomi_clone.provider

import com.example.tachiyomi_clone.data.local.database.DatabaseHandler
import com.example.tachiyomi_clone.data.local.database.listOfStringsAdapter
import com.example.tachiyomi_clone.data.local.database.mapper.mangaMapper
import com.example.tachiyomi_clone.data.local.database.updateStrategyAdapter
import com.example.tachiyomi_clone.data.model.dto.MangaDto
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.model.mapper.MangaMapper
import com.example.tachiyomi_clone.data.network.http.MangaSource
import com.example.tachiyomi_clone.data.network.http.await
import com.example.tachiyomi_clone.data.repository.MangaRepository
import com.example.tachiyomi_clone.utils.Logger
import com.example.tachiyomi_clone.utils.toLong
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

class MangaProvider @Inject constructor(
    private val handler: DatabaseHandler,
    private val client: OkHttpClient,
    private val source: MangaSource,
    private val mapper: MangaMapper
) : MangaRepository {

    companion object {
        const val TAG = "MangaProvider"
    }

    override suspend fun getMangaByUrl(url: String): MangaEntity? {
        return handler.awaitOneOrNull(inTransaction = true) {
            mangasQueries.getMangaByUrl(
                url,
                mangaMapper
            )
        }
    }

    override suspend fun insert(manga: MangaEntity): Long? {
        return handler.awaitOneOrNull(inTransaction = true) {
            mangasQueries.insert(
                url = manga.url,
                artist = manga.artist,
                author = manga.author,
                description = manga.description,
                genre = manga.genre,
                title = manga.title,
                status = manga.status,
                thumbnailUrl = manga.thumbnailUrl,
                favorite = manga.favorite,
                lastUpdate = manga.lastUpdate,
                nextUpdate = null,
                initialized = manga.initialized,
                viewerFlags = manga.viewerFlags,
                chapterFlags = manga.chapterFlags,
                coverLastModified = manga.coverLastModified,
                dateAdded = manga.dateAdded,
                updateStrategy = manga.updateStrategy,
            )
            mangasQueries.selectLastInsertedRowId()
        }
    }

    override suspend fun getMangaById(id: Long): MangaEntity {
        return handler.awaitOne { mangasQueries.getMangaById(id, mangaMapper) }
    }

    override suspend fun getMangaByIdAsFlow(id: Long): Flow<MangaEntity> {
        return handler.subscribeToOne { mangasQueries.getMangaById(id, mangaMapper) }
    }

    override suspend fun fetchMangaDetails(mangaUrl: String): MangaEntity {
        return client.newCall(source.mangaDetailsRequest(mangaUrl)).await().let {
            val dto = source.mangaDetailsParse(it).apply { initialized = true }
            return@let mapper.toEntity(dto)
        }
    }

    override suspend fun update(manga: MangaDto): Boolean {
        return try {
            partialUpdate(manga)
            true
        } catch (e: Exception) {
            Logger.e(TAG, "[${TAG}] update() --> error: ${e.message}")
            false
        }
    }

    private suspend fun partialUpdate(vararg mangas: MangaDto) {
        handler.await(inTransaction = true) {
            mangas.forEach { value ->
                mangasQueries.update(
                    url = value.url,
                    artist = value.artist,
                    author = value.author,
                    description = value.description,
                    genre = value.listGenre?.let(listOfStringsAdapter::encode),
                    title = value.title,
                    status = value.status?.toLong(),
                    thumbnailUrl = value.thumbnail_url,
                    favorite = value.favorite?.toLong(),
                    lastUpdate = value.lastUpdate,
                    initialized = value.initialized?.toLong(),
                    viewer = value.viewerFlags,
                    chapterFlags = value.chapterFlags,
                    coverLastModified = value.coverLastModified,
                    dateAdded = value.dateAdded,
                    mangaId = value.id ?: -1L,
                    updateStrategy = value.update_strategy?.let(updateStrategyAdapter::encode),
                )
            }
        }
    }
}