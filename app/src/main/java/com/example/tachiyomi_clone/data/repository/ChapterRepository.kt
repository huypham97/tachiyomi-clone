package com.example.tachiyomi_clone.data.repository

import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.dto.ChapterDto
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import kotlinx.coroutines.flow.Flow

interface ChapterRepository {

    suspend fun getChapterByMangaId(mangaId: Long): List<ChapterEntity>

    suspend fun getChapterByMangaIdAsFlow(mangaId: Long): Flow<List<ChapterEntity>>

//    suspend fun fetchChaptersFromNetwork(manga: MangaEntity): Flow<Result<List<ChapterDto>>>

    suspend fun fetchChaptersFromNetwork(manga: MangaEntity): Flow<Result<List<ChapterEntity>>>

    suspend fun removeChaptersWithIds(chapterIds: List<Long>)

    suspend fun addAll(chapters: List<ChapterEntity>): List<ChapterEntity>

    suspend fun updateAll(chapterUpdates: List<ChapterDto>)
}