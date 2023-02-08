package com.example.tachiyomi_clone.provider

import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.dto.MangasPageDto
import com.example.tachiyomi_clone.data.model.entity.MODULE_TYPE
import com.example.tachiyomi_clone.data.model.entity.MangaPagingSourceType
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import com.example.tachiyomi_clone.data.model.mapper.ErrorDataMapper
import com.example.tachiyomi_clone.data.model.mapper.MangasPageMapper
import com.example.tachiyomi_clone.data.network.service.HomeService
import com.example.tachiyomi_clone.data.repository.HomeRepository
import com.example.tachiyomi_clone.di.qualifier.DefaultDispatcher
import com.example.tachiyomi_clone.paging.home.HomePagingSource
import com.example.tachiyomi_clone.utils.Constant
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

    override fun fetchNewestMangaPage(): MangaPagingSourceType = object : HomePagingSource() {
        override suspend fun requestNextPage(currentPage: Int): MangasPageEntity {
            return homeService.newestMangaRequest(currentPage.toString()).let {
                mangasPageMapper.toEntity(
                    MangasPageDto.popularMangaParse(
                        it.body() ?: "",
                        it.raw()
                    )
                )
            }
        }
    }

    override fun fetchBoyMangaPage(): MangaPagingSourceType = object : HomePagingSource() {
        override suspend fun requestNextPage(currentPage: Int): MangasPageEntity {
            return homeService.boyMangaRequest(currentPage.toString()).let {
                mangasPageMapper.toEntity(
                    MangasPageDto.popularMangaParse(
                        it.body() ?: "",
                        it.raw()
                    )
                )
            }
        }
    }

    override fun fetchGirlMangaPage(): MangaPagingSourceType = object : HomePagingSource() {
        override suspend fun requestNextPage(currentPage: Int): MangasPageEntity {
            return homeService.girlMangaRequest(currentPage.toString()).let {
                mangasPageMapper.toEntity(
                    MangasPageDto.popularMangaParse(
                        it.body() ?: "",
                        it.raw()
                    )
                )
            }
        }
    }

    override fun fetchSearchMangaPage(keyword: String): MangaPagingSourceType =
        object : HomePagingSource() {
            override suspend fun requestNextPage(currentPage: Int): MangasPageEntity {
                return homeService.searchMangaRequest(
                    keyword = keyword,
                    page = currentPage.toString()
                ).let {
                    mangasPageMapper.toEntity(
                        MangasPageDto.popularMangaParse(
                            it.body() ?: "",
                            it.raw()
                        )
                    )
                }
            }
        }

    override fun fetchSearchByGenreMangaPage(keyword: String): MangaPagingSourceType =
        object : HomePagingSource() {
            override suspend fun requestNextPage(currentPage: Int): MangasPageEntity {
                var query = keyword
                for (genre in Constant.genreListUrl) {
                    if (genre.contains(keyword)) {
                        query = genre
                        break
                    }
                }
                return homeService.searchMangaByGenreRequest(
                    genre = query,
                    page = currentPage.toString()
                ).let {
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
                        type = MODULE_TYPE.NEWEST
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
                        type = MODULE_TYPE.POPULAR
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
                        type = MODULE_TYPE.GIRL
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
                        type = MODULE_TYPE.BOY
                    }
                )
                is Result.Error -> Result.Error(result.exception)
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }

}