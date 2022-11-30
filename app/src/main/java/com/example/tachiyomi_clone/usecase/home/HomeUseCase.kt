package com.example.tachiyomi_clone.usecase.home

import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.network.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    operator fun invoke(page: Int): Flow<Result<Any>> =
        homeRepository.fetchPopularManga(page)

}