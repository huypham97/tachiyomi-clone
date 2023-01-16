package com.example.tachiyomi_clone.usecase

import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.repository.ChapterRepository
import com.example.tachiyomi_clone.data.repository.MangaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetMangaWithChaptersUseCase @Inject constructor(
    private var mangaRepository: MangaRepository,
    private var chapterRepository: ChapterRepository
) {

    suspend fun subscribe(id: Long): Flow<Pair<MangaEntity, List<ChapterEntity>>> {
        return combine(
            mangaRepository.getMangaByIdAsFlow(id),
            chapterRepository.getChapterByMangaIdAsFlow(id),
        ) { manga, chapters ->
            Pair(manga, chapters)
        }
    }

    suspend fun awaitManga(id: Long): MangaEntity {
        return mangaRepository.getMangaById(id)
    }

    suspend fun awaitChapters(id: Long): List<ChapterEntity> {
        return chapterRepository.getChapterByMangaId(id)
    }

    suspend fun fetchMangaDetails(mangaUrl: String): MangaEntity {
        return mangaRepository.fetchMangaDetails(mangaUrl)
    }
}