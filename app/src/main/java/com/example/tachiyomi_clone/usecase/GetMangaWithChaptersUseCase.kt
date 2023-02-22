package com.example.tachiyomi_clone.usecase

import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.dao.MangaDao
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.model.mapper.ChapterMapper
import com.example.tachiyomi_clone.data.model.mapper.MangaMapper
import com.example.tachiyomi_clone.data.repository.ChapterRepository
import com.example.tachiyomi_clone.data.repository.MangaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import java.util.*
import javax.inject.Inject

class GetMangaWithChaptersUseCase @Inject constructor(
    private var mangaRepository: MangaRepository,
    private var chapterRepository: ChapterRepository,
    private val chapterMapper: ChapterMapper,
    private val mangaMapper: MangaMapper
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
        return chapterRepository.getChaptersByMangaId(id)
    }

    suspend fun fetchMangaDetails(manga: MangaEntity): Flow<Result<Pair<MangaEntity, List<ChapterEntity>>>> {
        manga.id = mangaRepository.getMangaByUrl(manga.url)?.id ?: -1L
        return mangaRepository.fetchMangaDetails(manga.url)
            .zip(getChapterList(manga)) { rsManga, rsChapters ->
                return@zip when {
                    rsManga is Result.Success && rsChapters is Result.Success -> Result.Success(
                        Pair(
                            rsManga.data,
                            rsChapters.data
                        )
                    )
                    rsManga is Result.Error -> Result.Error(rsManga.exception)
                    else -> Result.Error((rsChapters as Result.Error).exception)
                }
            }
    }

    suspend fun getChapterList(manga: MangaEntity): Flow<Result<List<ChapterEntity>>> {
        return chapterRepository.fetchChaptersFromNetwork(manga).map { result ->
            when (result) {
                is Result.Success -> Result.Success(updateChaptersLocal(result.data, manga))
                is Result.Error -> Result.Error(result.exception)
            }
        }
    }

    suspend fun addChapter(chapter: ChapterEntity) {
        val chapterLocal = chapterRepository.getChapterByUrl(chapter.url)
        if (chapterLocal == null)
            chapterRepository.addChapter(chapter)
    }

    private suspend fun updateChaptersLocal(
        rawSourceChapters: List<ChapterEntity>,
        manga: MangaEntity
    ): List<ChapterEntity> {
        if (rawSourceChapters.isEmpty()) throw Exception()

        val sourceChapters = rawSourceChapters.distinctBy { it.url }
        val dbChapters = chapterRepository.getChaptersByMangaId(manga.id)

        for (sourceChapter in sourceChapters) {
            val dbChapter = dbChapters.find { it.url == sourceChapter.url }
            if (dbChapter != null) {
                sourceChapter.read = true
            }
        }

        return sourceChapters
    }

    private suspend fun awaitUpdateLastUpdate(mangaId: Long): Boolean {
        return mangaRepository.updateToLocal(MangaDao(id = mangaId, lastUpdate = Date().time))
    }

    private fun shouldUpdateDbChapter(
        dbChapter: ChapterEntity, sourceChapter: ChapterEntity
    ): Boolean {
        return dbChapter.name != sourceChapter.name || dbChapter.dateUpload != sourceChapter.dateUpload || dbChapter.sourceOrder != sourceChapter.sourceOrder
    }
}