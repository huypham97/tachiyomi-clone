package com.example.tachiyomi_clone.data.network.service

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface HomeService {

    @Headers("Content-Type: application/json")
    @GET("hot")
    suspend fun popularMangaRequest(@Query("page") page: Int): Any
}