package com.example.tachiyomi_clone.data.network.repository

import kotlinx.coroutines.flow.Flow
import com.example.tachiyomi_clone.data.model.Result

interface HomeRepository {
    fun fetchPopularManga(page: Int): Flow<Result<Any>>
}