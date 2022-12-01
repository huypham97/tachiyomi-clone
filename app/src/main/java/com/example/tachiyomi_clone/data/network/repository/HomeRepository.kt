package com.example.tachiyomi_clone.data.network.repository

import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.dto.MangasPageDto
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.Response

interface HomeRepository {
    fun fetchPopularManga(page: Int): Flow<Result<MangasPageEntity>>
}