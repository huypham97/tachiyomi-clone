package com.example.tachiyomi_clone.data.network.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface HomeService {

    @Headers("Content-Type: text/html")
    @GET("hot")
    suspend fun popularMangaRequest(@Query("page") page: String): Response<String>

    @Headers("Content-Type: text/html")
    @GET("{manga_url}")
    suspend fun mangaDetailsRequest(@Path("manga_url") mangaUrl: String): Response<String>

    @Headers("Content-Type: text/html")
    @GET("{manga_url}")
    suspend fun chapterListRequest(@Path("manga_url") mangaUrl: String): Response<String>

    @Headers("Content-Type: text/html")
    @GET("{chapter_url}")
    suspend fun pageListRequest(@Path("chapter_url") chapterUrl: String): Response<String>

    @Headers("Content-Type: text/html")
    @GET
    suspend fun imageRequest(@Url url: String): Response<String>
}