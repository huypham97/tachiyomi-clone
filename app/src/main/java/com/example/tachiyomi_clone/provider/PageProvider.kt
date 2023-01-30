package com.example.tachiyomi_clone.provider

import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.data
import com.example.tachiyomi_clone.data.model.entity.PageEntity
import com.example.tachiyomi_clone.data.model.mapper.ErrorDataMapper
import com.example.tachiyomi_clone.data.model.mapper.PageMapper
import com.example.tachiyomi_clone.data.network.http.MangaSource
import com.example.tachiyomi_clone.data.network.http.await
import com.example.tachiyomi_clone.data.network.service.HomeService
import com.example.tachiyomi_clone.data.repository.PageRepository
import com.example.tachiyomi_clone.di.qualifier.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import okhttp3.OkHttpClient
import javax.inject.Inject

class PageProvider @Inject constructor(
    private val client: OkHttpClient,
    private val source: MangaSource,
    private val mapper: PageMapper,
    private val service: HomeService,
    errorDataMapper: ErrorDataMapper,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : PageRepository, BaseProvider(defaultDispatcher, errorDataMapper) {

    companion object {
        const val TAG = "PageProvider"
    }

    @OptIn(FlowPreview::class)
    override suspend fun fetchPageList(chapterUrl: String): Flow<Result<PageEntity>> {
        return flow {
            val a = safeApiCall {
                service.pageListRequest(chapterUrl).let {
                    val listDto = source.pageListParse(it.body(), it.raw())
                    return@let listDto.map { dto -> mapper.toEntity(dto) }
                }
            }
            when (a) {
                is Result.Success -> a.data.forEach { emit(Result.Success(it)) }
                else -> emit(Result.Error((a as Result.Error).exception))
            }
        }.flatMapMerge {
            parseImageToBitmap(it)
        }
    }

    override suspend fun parseImageToBitmap(page: Result<PageEntity>): Flow<Result<PageEntity>> {
        return flow {
            if (page.data != null)
                emit(safeApiCall {
                    client.newCall(source.imageRequest(page.data!!.imageUrl ?: "")).await().let {
                        return@let page.data!!.apply { byte = it.body.bytes() }
                    }
                })
            else emit(page)
        }.map { result ->
            when (result) {
                is Result.Success -> Result.Success(result.data)
                is Result.Error -> Result.Error(result.exception)
                else -> Result.Error(IllegalStateException("Result must be Success or Error"))
            }
        }
    }

}
