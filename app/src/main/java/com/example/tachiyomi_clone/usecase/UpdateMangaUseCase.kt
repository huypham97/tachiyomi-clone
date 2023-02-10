package com.example.tachiyomi_clone.usecase

import com.example.tachiyomi_clone.data.model.dao.MangaDao
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.repository.MangaRepository
import javax.inject.Inject

class UpdateMangaUseCase @Inject constructor(private val mangaRepository: MangaRepository) {

    suspend fun awaitUpdateFromSource(
        manga: MangaEntity,
    ): Boolean {
        val title = manga.title.ifEmpty { null }
        val thumbnailUrl = manga.thumbnailUrl?.takeIf { it.isNotEmpty() }
        return mangaRepository.updateToLocal(
            MangaDao(
                id = manga.id,
                url = manga.url,
                title = title,
                artist = manga.artist,
                author = manga.author,
                description = manga.description,
                status = manga.status.toInt(),
                thumbnail_url = thumbnailUrl,
                update_strategy = manga.updateStrategy,
                initialized = true,
                coverLastModified = 0L,
                favorite = manga.favorite
            )
        )
    }

}