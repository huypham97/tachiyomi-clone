package com.example.tachiyomi_clone.provider

import com.example.tachiyomi_clone.data.model.entity.PageEntity
import com.example.tachiyomi_clone.data.model.mapper.PageMapper
import com.example.tachiyomi_clone.data.network.http.MangaSource
import com.example.tachiyomi_clone.data.network.http.await
import com.example.tachiyomi_clone.data.repository.PageRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import javax.inject.Inject

class PageProvider @Inject constructor(
    private val client: OkHttpClient,
    private val source: MangaSource,
    private val mapper: PageMapper
) : PageRepository {

    companion object {
        const val TAG = "PageProvider"
    }

    @OptIn(FlowPreview::class)
    override suspend fun fetchPageList(chapterUrl: String): Flow<PageEntity> {
        return client.newCall(source.pageListRequest(chapterUrl)).await().let {
            val listDto = source.pageListParse(it)
            return@let listDto.map { dto -> mapper.toEntity(dto) }
        }.asFlow().flatMapMerge {
            parseImageToBitmap(it)
        }
    }

    override suspend fun parseImageToBitmap(page: PageEntity): Flow<PageEntity> {
        return flow {
            emit(client.newCall(source.imageRequest(page.imageUrl ?: "")).await().let {
                return@let page.apply { byte = it.body.bytes() }
            })
        }
    }
}