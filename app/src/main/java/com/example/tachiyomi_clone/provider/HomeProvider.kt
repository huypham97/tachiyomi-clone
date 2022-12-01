package com.example.tachiyomi_clone.provider

import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import com.example.tachiyomi_clone.data.model.mapper.ErrorDataMapper
import com.example.tachiyomi_clone.data.model.mapper.MangasPageMapper
import com.example.tachiyomi_clone.data.network.http.MangaSource
import com.example.tachiyomi_clone.data.network.http.await
import com.example.tachiyomi_clone.data.network.repository.HomeRepository
import com.example.tachiyomi_clone.di.qualifier.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import javax.inject.Inject

class HomeProvider @Inject constructor(
    private val client: OkHttpClient,
    private val source: MangaSource,
    private val mangasPageMapper: MangasPageMapper,
    errorDataMapper: ErrorDataMapper,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : HomeRepository, BaseProvider(defaultDispatcher, errorDataMapper) {
    override fun fetchPopularManga(page: Int): Flow<Result<MangasPageEntity>> =
        flow {
            emit(safeApiCall {
                client.newCall(source.popularMangaRequest(page)).await()
            })
        }.map { result ->
            when (result) {
                is Result.Success -> Result.Success(
                    mangasPageMapper.toEntity(
                        source.popularMangaParse(
                            result.data
                        )
                    )
                )
                is Result.Error -> Result.Error(result.exception)
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }

}