package com.example.tachiyomi_clone.provider

import com.example.tachiyomi_clone.data.model.entity.MangaPagingSourceType
import com.example.tachiyomi_clone.data.model.mapper.MangasPageMapper
import com.example.tachiyomi_clone.data.network.http.MangaSource
import com.example.tachiyomi_clone.data.repository.HomeRepository
import com.example.tachiyomi_clone.paging.home.HomePopularPagingSource
import okhttp3.OkHttpClient
import javax.inject.Inject

class HomeProvider @Inject constructor(
    private val client: OkHttpClient,
    private val source: MangaSource,
    private val mangasPageMapper: MangasPageMapper,
) : HomeRepository {
    override fun fetchPopularManga(): MangaPagingSourceType =
        HomePopularPagingSource(client, source, mangasPageMapper)

}