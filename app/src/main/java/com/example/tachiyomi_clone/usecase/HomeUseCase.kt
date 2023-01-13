package com.example.tachiyomi_clone.usecase

import com.example.tachiyomi_clone.data.model.entity.MangaPagingSourceType
import com.example.tachiyomi_clone.data.repository.HomeRepository
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    fun subscribe(query: String): MangaPagingSourceType {
        return when (query) {
            QUERY_POPULAR -> homeRepository.fetchPopularManga()
            else -> homeRepository.fetchPopularManga()
        }
    }

    companion object {
        const val QUERY_POPULAR = "eu.kanade.domain.source.interactor.POPULAR"
        const val QUERY_LATEST = "eu.kanade.domain.source.interactor.LATEST"
    }
}