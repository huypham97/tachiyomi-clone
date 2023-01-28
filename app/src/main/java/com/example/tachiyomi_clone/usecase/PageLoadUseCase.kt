package com.example.tachiyomi_clone.usecase

import com.example.tachiyomi_clone.data.model.entity.PageEntity
import com.example.tachiyomi_clone.data.repository.PageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PageLoadUseCase @Inject constructor(private val pageRepository: PageRepository) {

    suspend fun fetchPageList(chapterUrl: String): Flow<PageEntity> {
        return pageRepository.fetchPageList(chapterUrl)
    }

}