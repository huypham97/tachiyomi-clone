package com.example.tachiyomi_clone.provider

import com.example.tachiyomi_clone.data.model.dto.MangasPageDto
import com.example.tachiyomi_clone.data.model.entity.MangaPagingSourceType
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import com.example.tachiyomi_clone.data.model.mapper.MangasPageMapper
import com.example.tachiyomi_clone.data.network.service.HomeService
import com.example.tachiyomi_clone.data.repository.HomeRepository
import com.example.tachiyomi_clone.paging.home.HomePagingSource
import javax.inject.Inject

class HomeProvider @Inject constructor(
    private val mangasPageMapper: MangasPageMapper,
    private val homeService: HomeService
) : HomeRepository {
    override fun fetchPopularManga(): MangaPagingSourceType =
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

}