package com.example.tachiyomi_clone.usecase

import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.entity.MangaPagingSourceType
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import com.example.tachiyomi_clone.data.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    fun getPopularMangaPage(): MangaPagingSourceType {
        return homeRepository.fetchPopularMangaPage()
    }

    suspend fun getPopularManga(): Flow<Result<MangasPageEntity>> {
        return homeRepository.fetchPopularManga()
    }
}