package com.example.tachiyomi_clone.paging.home

import androidx.paging.PagingState
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.model.entity.MangaPagingSourceType
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import com.example.tachiyomi_clone.utils.withIOContext

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
