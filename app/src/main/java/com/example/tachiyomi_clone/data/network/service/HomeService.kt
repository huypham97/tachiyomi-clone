package com.example.tachiyomi_clone.data.network.service

import retrofit2.http.GET
import retrofit2.http.Query

interface HomeService {

    @GET("hot/")
    suspend fun popularMangaRequest(@Query("page") page: Int): Any
}