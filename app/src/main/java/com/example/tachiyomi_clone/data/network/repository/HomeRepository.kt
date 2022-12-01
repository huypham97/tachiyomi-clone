package com.example.tachiyomi_clone.data.network.repository

import com.example.tachiyomi_clone.data.model.Result
import kotlinx.coroutines.flow.Flow
import okhttp3.Response

interface HomeRepository {
    fun fetchPopularManga(page: Int): Flow<Result<Response>>
}