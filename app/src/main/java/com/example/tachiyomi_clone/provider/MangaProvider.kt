package com.example.tachiyomi_clone.provider

import com.example.tachiyomi_clone.data.local.database.DatabaseHandler
import com.example.tachiyomi_clone.data.local.database.mapper.mangaMapper
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.repository.MangaRepository
import javax.inject.Inject

class MangaProvider @Inject constructor(private val handler: DatabaseHandler) : MangaRepository {
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
}