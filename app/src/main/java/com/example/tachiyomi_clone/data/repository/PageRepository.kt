package com.example.tachiyomi_clone.data.repository

import com.example.tachiyomi_clone.data.model.entity.PageEntity
import kotlinx.coroutines.flow.Flow

interface PageRepository {

    suspend fun fetchPageList(chapterUrl: String): Flow<PageEntity>

    suspend fun parseImageToBitmap(page: PageEntity): Flow<PageEntity>

}