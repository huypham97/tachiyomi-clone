package com.example.tachiyomi_clone.data.repository

import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.entity.MangaPagingSourceType
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun fetchPopularMangaPage(): MangaPagingSourceType

    fun fetchNewestMangaPage(): MangaPagingSourceType

    fun fetchBoyMangaPage(): MangaPagingSourceType

    fun fetchGirlMangaPage(): MangaPagingSourceType

    fun fetchSearchMangaPage(keyword: String): MangaPagingSourceType

    fun fetchSearchByGenreMangaPage(keyword: String): MangaPagingSourceType

    suspend fun fetchSuggestManga(): Flow<Result<MangasPageEntity>>

    suspend fun fetchNewestManga(): Flow<Result<MangasPageEntity>>

    suspend fun fetchPopularManga(): Flow<Result<MangasPageEntity>>

    suspend fun fetchGirlManga(): Flow<Result<MangasPageEntity>>

    suspend fun fetchBoyManga(): Flow<Result<MangasPageEntity>>
}