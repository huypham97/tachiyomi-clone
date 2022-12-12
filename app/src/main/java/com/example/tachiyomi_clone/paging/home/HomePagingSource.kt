package com.example.tachiyomi_clone.paging.home

import androidx.paging.PagingState
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.model.entity.MangaPagingSourceType
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import com.example.tachiyomi_clone.data.model.mapper.MangasPageMapper
import com.example.tachiyomi_clone.data.network.http.MangaSource
import com.example.tachiyomi_clone.data.network.http.await
import com.example.tachiyomi_clone.utils.withIOContext
import okhttp3.OkHttpClient

abstract class HomePagingSource : MangaPagingSourceType() {

    abstract suspend fun requestNextPage(currentPage: Int): MangasPageEntity

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, MangaEntity> {
        val page = params.key ?: 1

        val mangasPage = try {
            withIOContext {
                requestNextPage(page.toInt()).takeIf { !it.mangas.isNullOrEmpty() }
                    ?: throw Exception()
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }

        return LoadResult.Page(
            data = mangasPage.mangas ?: listOf(),
            prevKey = null,
            nextKey = if (mangasPage.hasNextPage == true) page + 1 else null,
        )
    }

    override fun getRefreshKey(state: PagingState<Long, MangaEntity>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }
}

class HomePopularPagingSource constructor(
    private val client: OkHttpClient,
    private val source: MangaSource,
    private val mangasPageMapper: MangasPageMapper
) : HomePagingSource() {
    override suspend fun requestNextPage(currentPage: Int): MangasPageEntity {
        return client.newCall(source.popularMangaRequest(currentPage)).await().let {
            mangasPageMapper.toEntity(source.popularMangaParse(it))
        }
    }

}