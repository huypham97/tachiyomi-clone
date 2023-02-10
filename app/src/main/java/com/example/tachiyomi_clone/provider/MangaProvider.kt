package com.example.tachiyomi_clone.provider

import com.example.tachiyomi_clone.data.local.database.DatabaseHandler
import com.example.tachiyomi_clone.data.local.database.mapper.mangaMapper
import com.example.tachiyomi_clone.data.local.database.updateStrategyAdapter
import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.dao.MangaDao
import com.example.tachiyomi_clone.data.model.dto.MangaDto
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.model.entity.toDomain
import com.example.tachiyomi_clone.data.model.mapper.ErrorDataMapper
import com.example.tachiyomi_clone.data.model.mapper.MangaMapper
import com.example.tachiyomi_clone.data.network.service.HomeService
import com.example.tachiyomi_clone.data.repository.MangaRepository
import com.example.tachiyomi_clone.di.qualifier.DefaultDispatcher
import com.example.tachiyomi_clone.utils.Logger
import com.example.tachiyomi_clone.utils.toLong
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MangaProvider @Inject constructor(
    private val handler: DatabaseHandler,
    private val mapper: MangaMapper,
    private val service: HomeService,
    errorDataMapper: ErrorDataMapper,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : MangaRepository, BaseProvider(defaultDispatcher, errorDataMapper) {

    companion object {
        const val TAG = "MangaProvider"
    }

    override suspend fun getMangaByUrl(url: String): MangaEntity? {
        return handler.awaitOneOrNull(inTransaction = true) {
            mangasQueries.getMangaByUrl(
                url,
                mangaMapper
            )
        }?.toDomain()
    }

    override suspend fun getMangasFavorite(): List<MangaEntity> {
        return handler.awaitList(inTransaction = true) {
            mangasQueries.getFavorites(mangaMapper)
        }.map { it.toDomain() }
    }

    override suspend fun insert(manga: MangaEntity): Long? {
        return handler.awaitOneOrNull(inTransaction = true) {
            mangasQueries.insert(
                url = manga.url,
                artist = manga.artist,
                author = manga.author,
                description = manga.description,
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
        return handler.awaitOne { mangasQueries.getMangaById(id, mangaMapper) }.toDomain()
    }

    override suspend fun getMangaByIdAsFlow(id: Long): Flow<MangaEntity> {
        return handler.subscribeToOne { mangasQueries.getMangaById(id, mangaMapper) }.map {
            it.toDomain()
        }
    }

    override suspend fun fetchMangaDetails(mangaUrl: String): Flow<Result<MangaEntity>> {
        return flow {
            emit(safeApiCall {
                service.mangaDetailsRequest(mangaUrl)
            })
        }.map { result ->
            when (result) {
                is Result.Success -> Result.Success(
                    mapper.toEntity(
                        MangaDto.mangaDetailsParse(result.data.body(), result.data.raw())
                            .apply { initialized = true })
                )
                is Result.Error -> Result.Error(result.exception)
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }

    override suspend fun updateToLocal(manga: MangaDao): Boolean {
        return try {
            partialUpdate(manga)
            true
        } catch (e: Exception) {
            Logger.e(TAG, "[${TAG}] update() --> error: ${e.message}")
            false
        }
    }

    private suspend fun partialUpdate(vararg mangas: MangaDao) {
        handler.await(inTransaction = true) {
            mangas.forEach { value ->
                mangasQueries.update(
                    url = value.url,
                    artist = value.artist,
                    author = value.author,
                    description = value.description,
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