package com.example.tachiyomi_clone.usecase

import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.repository.MangaRepository
import javax.inject.Inject

class NetworkToLocalUseCase @Inject constructor(private val mangaRepository: MangaRepository) {

    suspend fun await(manga: MangaEntity): MangaEntity {
        val localManga = getManga(manga.url)
        return when {
            localManga == null -> {
                val id = insertManga(manga)
                manga.copy(id = id!!)
            }
            !localManga.favorite -> {
                // if the manga isn't a favorite, set its display title from source
                // if it later becomes a favorite, updated title will go to db
                localManga.copy(title = manga.title)
            }
            else -> {
                localManga
            }
        }
    }

    private suspend fun getManga(url: String): MangaEntity? {
        return mangaRepository.getMangaByUrl(url)
    }

    private suspend fun insertManga(manga: MangaEntity): Long? {
        return mangaRepository.insert(manga)
    }

}