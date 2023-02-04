package com.example.tachiyomi_clone.provider

import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.dto.MangasPageDto
import com.example.tachiyomi_clone.data.model.entity.MangaPagingSourceType
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import com.example.tachiyomi_clone.data.model.mapper.ErrorDataMapper
import com.example.tachiyomi_clone.data.model.mapper.MangasPageMapper
import com.example.tachiyomi_clone.data.network.service.HomeService
import com.example.tachiyomi_clone.data.repository.HomeRepository
import com.example.tachiyomi_clone.di.qualifier.DefaultDispatcher
import com.example.tachiyomi_clone.paging.home.HomePagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeProvider @Inject constructor(
    private val mangasPageMapper: MangasPageMapper,
    private val homeService: HomeService,
    errorDataMapper: ErrorDataMapper,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : BaseProvider(defaultDispatcher, errorDataMapper), HomeRepository {

    override fun fetchPopularMangaPage(): MangaPagingSourceType =
        object : HomePagingSource() {
            override suspend fun requestNextPage(currentPage: Int): MangasPageEntity {
                return homeService.popularMangaRequest(currentPage.toString()).let {
                    mangasPageMapper.toEntity(
                        MangasPageDto.popularMangaParse(
                            it.body() ?: "",
                            it.raw()
                        )
                    )
                }
            }

        }

    override suspend fun fetchSuggestManga(): Flow<Result<MangasPageEntity>> {
        return flow {
            emit(safeApiCall {
                homeService.popularMangaRequest("")
            })
        }.map { result ->
            when (result) {
                is Result.Success -> Result.Success(
                    mangasPageMapper.toEntity(
                        MangasPageDto.suggestMangaParse(
                            result.data.body() ?: "",
                            result.data.raw()
                        )
                    )
                )
                is Result.Error -> Result.Error(result.exception)
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }

    override suspend fun fetchNewestManga(): Flow<Result<MangasPageEntity>> {
        return flow {
            emit(safeApiCall {
                homeService.newestMangaRequest("")
            })
        }.map { result ->
            when (result) {
                is Result.Success -> Result.Success(
                    mangasPageMapper.toEntity(
                        MangasPageDto.suggestMangaParse(
                            result.data.body() ?: "",
                            result.data.raw()
                        )
                    ).apply {
                        title = "Truyện mới cập nhật"
                    }
                )
                is Result.Error -> Result.Error(result.exception)
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }

    override suspend fun fetchPopularManga(): Flow<Result<MangasPageEntity>> {
        return flow {
            emit(safeApiCall {
                homeService.popularMangaRequest("")
            })
        }.map { result ->
            when (result) {
                is Result.Success -> Result.Success(
                    mangasPageMapper.toEntity(
                        MangasPageDto.popularMangaParse(
                            result.data.body() ?: "",
                            result.data.raw()
                        )
                    ).apply {
                        title = "Truyện phổ biến"
                    }
                )
                is Result.Error -> Result.Error(result.exception)
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }

    override suspend fun fetchGirlManga(): Flow<Result<MangasPageEntity>> {
        return flow {
            emit(safeApiCall {
                homeService.girlMangaRequest("")
            })
        }.map { result ->
            when (result) {
                is Result.Success -> Result.Success(
                    mangasPageMapper.toEntity(
                        MangasPageDto.popularMangaParse(
                            result.data.body() ?: "",
                            result.data.raw()
                        )
                    ).apply {
                        title = "Truyện con gái"
                    }
                )
                is Result.Error -> Result.Error(result.exception)
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }

    override suspend fun fetchBoyManga(): Flow<Result<MangasPageEntity>> {
        return flow {
            emit(safeApiCall {
                homeService.boyMangaRequest("")
            })
        }.map { result ->
            when (result) {
                is Result.Success -> Result.Success(
                    mangasPageMapper.toEntity(
                        MangasPageDto.popularMangaParse(
                            result.data.body() ?: "",
                            result.data.raw()
                        )
                    ).apply {
                        title = "Truyện con trai"
                    }
                )
                is Result.Error -> Result.Error(result.exception)
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }

}