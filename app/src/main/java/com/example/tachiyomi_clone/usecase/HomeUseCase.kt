package com.example.tachiyomi_clone.usecase

import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.entity.MangaPagingSourceType
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import com.example.tachiyomi_clone.data.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    fun getPopularMangaPage(): MangaPagingSourceType {
        return homeRepository.fetchPopularMangaPage()
    }

    suspend fun getSuggestManga(): Flow<Result<MangasPageEntity>> {
        return homeRepository.fetchSuggestManga()
    }

    suspend fun getModulesManga(): Flow<Result<MutableList<MangasPageEntity>>> {
        return homeRepository.fetchNewestManga()
            .zip(homeRepository.fetchPopularManga()) { newest, populars ->
                mutableListOf(newest, populars)
            }.zip(homeRepository.fetchBoyManga()) { list, boys ->
                list.apply { add(boys) }
            }.zip(homeRepository.fetchGirlManga()) { list, girls ->
                list.apply { add(girls) }
            }.map { results ->
                val listResult: MutableList<MangasPageEntity> = mutableListOf()
                results.forEach { result ->
                    when (result) {
                        is Result.Success -> listResult.add(result.data)
                        is Result.Error -> {
                            return@map Result.Error(result.exception)
                        }
                    }
                }
                return@map Result.Success(listResult)
            }
    }
}