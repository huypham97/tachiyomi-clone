package com.example.tachiyomi_clone.usecase

import com.example.tachiyomi_clone.data.model.dto.MangaDto
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.repository.MangaRepository
import javax.inject.Inject

class UpdateMangaUseCase @Inject constructor(private val mangaRepository: MangaRepository) {

    suspend fun awaitUpdateFromSource(
        localManga: MangaEntity,
        remoteManga: MangaEntity,
        manualFetch: Boolean,
    ): Boolean {
        val title = remoteManga.title.ifEmpty { null }
        val thumbnailUrl = remoteManga.thumbnailUrl?.takeIf { it.isNotEmpty() }
        return mangaRepository.update(
            MangaDto(
                id = localManga.id,
                title = remoteManga.title,
                coverLastModified = 0L,
                author = remoteManga.author,
                artist = remoteManga.artist,
                description = remoteManga.description,
                thumbnail_url = thumbnailUrl,
                status = remoteManga.status.toInt(),
                update_strategy = remoteManga.updateStrategy,
                initialized = true,
            ).apply {
                listGenre = remoteManga.genre
            }
        )
    }

}