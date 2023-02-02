package com.example.tachiyomi_clone.data.repository

import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.entity.MangaPagingSourceType
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun fetchPopularMangaPage(): MangaPagingSourceType

    suspend fun fetchPopularManga(): Flow<Result<MangasPageEntity>>
}