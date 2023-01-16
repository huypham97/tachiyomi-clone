package com.example.tachiyomi_clone.data.repository

import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import kotlinx.coroutines.flow.Flow

interface ChapterRepository {

    suspend fun getChapterByMangaId(mangaId: Long): List<ChapterEntity>

    suspend fun getChapterByMangaIdAsFlow(mangaId: Long): Flow<List<ChapterEntity>>

    suspend fun fetchChaptersFromNetwork(manga: MangaEntity): List<ChapterEntity>
}