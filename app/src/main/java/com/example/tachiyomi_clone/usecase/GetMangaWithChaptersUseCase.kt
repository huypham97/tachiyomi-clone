package com.example.tachiyomi_clone.usecase

import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.dto.ChapterDto
import com.example.tachiyomi_clone.data.model.dto.MangaDto
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.model.mapper.ChapterMapper
import com.example.tachiyomi_clone.data.model.mapper.MangaMapper
import com.example.tachiyomi_clone.data.repository.ChapterRepository
import com.example.tachiyomi_clone.data.repository.MangaRepository
import com.example.tachiyomi_clone.provider.ChapterProvider
import com.example.tachiyomi_clone.utils.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
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
        return chapterRepository.getChapterByMangaId(id)
    }

    suspend fun fetchMangaDetails(mangaUrl: String): Flow<Result<MangaEntity>> {
        return mangaRepository.fetchMangaDetails(mangaUrl)
    }

    suspend fun getChapterList(manga: MangaEntity): Flow<Result<List<ChapterEntity>>> {
        return chapterRepository.fetchChaptersFromNetwork(manga).map { result ->
            when (result) {
                is Result.Success -> Result.Success(updateChaptersLocal(result.data, manga))
                is Result.Error -> Result.Error(result.exception)
            }
        }
    }

    private suspend fun updateChaptersLocal(
        rawSourceChapters: List<ChapterDto>,
        manga: MangaEntity
    ): List<ChapterEntity> {
        if (rawSourceChapters.isEmpty()) throw Exception()

        val sourceChapters = rawSourceChapters.distinctBy { it.url }
            .mapIndexed { i, dto ->
                chapterMapper.toEntity(dto)
                    .apply {
                        name = dto.name?.let { cleanupChapterName(it, manga.title) } ?: ""
                        url = dto.url ?: ""
                        dateUpload = dto.date_upload
                        chapterNumber = dto.chapter_number
                        scanlator = dto.scanlator?.ifBlank { null }
                        mangaId = manga.id
                        sourceOrder = i.toLong()
                    }
            }

        val dbChapters = chapterRepository.getChapterByMangaId(manga.id)
        val toAdd = mutableListOf<ChapterEntity>()
        val toChange = mutableListOf<ChapterEntity>()
        val toDelete = dbChapters.filterNot { dbChapter ->
            sourceChapters.any { sourceChapter ->
                dbChapter.url == sourceChapter.url
            }
        }
        val rightNow = Date().time
        var maxSeenUploadDate = 0L

        for (sourceChapter in sourceChapters) {
            val chapter = sourceChapter
            val chapterNumber = chapter.parseChapterNumber(
                manga.title,
                chapter.name,
                chapter.chapterNumber
            )
            chapter.chapterNumber = chapterNumber

            val dbChapter = dbChapters.find { it.url == chapter.url }

            if (dbChapter == null) {
                val toAddChapter = if (chapter.dateUpload == 0L) {
                    val altDateUpload = if (maxSeenUploadDate == 0L) rightNow else maxSeenUploadDate
                    chapter.copy(dateUpload = altDateUpload)
                } else {
                    maxSeenUploadDate =
                        java.lang.Long.max(maxSeenUploadDate, sourceChapter.dateUpload)
                    chapter
                }
                toAdd.add(toAddChapter)
            } else {
                if (shouldUpdateDbChapter(dbChapter, chapter)) {
                    var toChangeChapter = dbChapter.copy(
                        name = chapter.name,
                        chapterNumber = chapter.chapterNumber,
                        scanlator = chapter.scanlator,
                        sourceOrder = chapter.sourceOrder,
                    )
                    if (chapter.dateUpload != 0L) {
                        toChangeChapter = toChangeChapter.copy(dateUpload = chapter.dateUpload)
                    }
                    toChange.add(toChangeChapter)
                }
            }
        }

        if (toAdd.isEmpty() && toDelete.isEmpty() && toChange.isEmpty()) {
            return emptyList()
        }

        val reAdded = mutableListOf<ChapterEntity>()

        val deletedChapterNumbers = TreeSet<Float>()
        val deletedReadChapterNumbers = TreeSet<Float>()
        val deletedBookmarkedChapterNumbers = TreeSet<Float>()

        toDelete.forEach { chapter ->
            if (chapter.read) deletedReadChapterNumbers.add(chapter.chapterNumber)
            if (chapter.bookmark) deletedBookmarkedChapterNumbers.add(chapter.chapterNumber)
            deletedChapterNumbers.add(chapter.chapterNumber)
        }

        val deletedChapterNumberDateFetchMap = toDelete.sortedByDescending { it.dateFetch }
            .associate { it.chapterNumber to it.dateFetch }

        var itemCount = toAdd.size
        var updatedToAdd = toAdd.map { toAddItem ->
            var chapter = toAddItem.copy(dateFetch = rightNow + itemCount--)

            if (chapter.isRecognizedNumber.not() || chapter.chapterNumber !in deletedChapterNumbers)
                return@map chapter

            chapter = chapter.copy(
                read = chapter.chapterNumber in deletedReadChapterNumbers,
                bookmark = chapter.chapterNumber in deletedBookmarkedChapterNumbers,
            )

            // Try to to use the fetch date of the original entry to not pollute 'Updates' tab
            deletedChapterNumberDateFetchMap[chapter.chapterNumber]?.let {
                chapter = chapter.copy(dateFetch = it)
            }

            reAdded.add(chapter)

            chapter
        }

        if (toDelete.isNotEmpty()) {
            val toDeleteIds = toDelete.map { it.id }
            chapterRepository.removeChaptersWithIds(toDeleteIds)
        }

        if (updatedToAdd.isNotEmpty()) {
            updatedToAdd = chapterRepository.addAll(updatedToAdd)
        }

        if (toChange.isNotEmpty()) {
            val chapterUpdates = toChange.map { chapterMapper.fromEntity(it) }
            try {
                chapterRepository.updateAll(chapterUpdates)
            } catch (e: Exception) {
                Logger.e(
                    ChapterProvider.TAG,
                    "[${ChapterProvider.TAG}] updateChaptersLocal() --> error: ${e.message}"
                )
            }
        }

        // Set this manga as updated since chapters were changed
        // Note that last_update actually represents last time the chapter list changed at all
        awaitUpdateLastUpdate(manga.id)

        val reAddedUrls = reAdded.map { it.url }.toHashSet()

        return updatedToAdd.filterNot { it.url in reAddedUrls }
    }

    private suspend fun awaitUpdateLastUpdate(mangaId: Long): Boolean {
        return mangaRepository.updateToLocal(MangaDto(id = mangaId, lastUpdate = Date().time))
    }

    private fun shouldUpdateDbChapter(
        dbChapter: ChapterEntity, sourceChapter: ChapterEntity
    ): Boolean {
        return dbChapter.scanlator != sourceChapter.scanlator || dbChapter.name != sourceChapter.name || dbChapter.dateUpload != sourceChapter.dateUpload || dbChapter.chapterNumber != sourceChapter.chapterNumber || dbChapter.sourceOrder != sourceChapter.sourceOrder
    }
}