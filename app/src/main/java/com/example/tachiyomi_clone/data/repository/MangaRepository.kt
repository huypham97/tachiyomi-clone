package com.example.tachiyomi_clone.data.repository

import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.dto.MangaDto
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import kotlinx.coroutines.flow.Flow

interface MangaRepository {

    suspend fun getMangaByUrl(url: String): MangaEntity?

//    suspend fun insert(mangaEntity: MangaEntity): Long?

    suspend fun getMangaById(id: Long): MangaEntity

    suspend fun getMangaByIdAsFlow(id: Long): Flow<MangaEntity>

    suspend fun fetchMangaDetails(mangaUrl: String): Flow<Result<MangaEntity>>

    suspend fun updateToLocal(update: MangaDto): Boolean
}