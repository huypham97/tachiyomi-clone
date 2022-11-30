package com.example.tachiyomi_clone.provider

import com.example.tachiyomi_clone.data.network.repository.HomeRepository
import com.example.tachiyomi_clone.data.network.service.HomeService
import com.example.tachiyomi_clone.di.qualifier.DefaultDispatcher
import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.mapper.ErrorDataMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeProvider @Inject constructor(
    private val homeService: HomeService,
    errorDataMapper: ErrorDataMapper,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : HomeRepository, BaseProvider(defaultDispatcher, errorDataMapper) {
    override fun fetchPopularManga(page: Int): Flow<Result<Any>> =
        flow {
            emit(safeApiCall {
                homeService.popularMangaRequest(page)
            })
        }.map { result ->
            when (result) {
                is Result.Success -> Result.Success(
                    result
                )
                is Result.Error -> Result.Error(result.exception)
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }

}