package com.example.tachiyomi_clone.data.network.service

import com.example.tachiyomi_clone.utils.Constant
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

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

    @GET
    @Headers("Referer: ${Constant.BASE_URL}")
    suspend fun imageRequest(@Url url: String): ResponseBody

    @Headers("Content-Type: text/html")
    @GET("truyen-con-gai")
    suspend fun girlMangaRequest(@Query("page") page: String): Response<String>

    @Headers("Content-Type: text/html")
    @GET("truyen-con-trai")
    suspend fun boyMangaRequest(@Query("page") page: String): Response<String>

    @Headers("Content-Type: text/html")
    @GET(".")
    suspend fun newestMangaRequest(@Query("page") page: String): Response<String>

    @Headers("Content-Type: text/html")
    @GET("tim-truyen")
    suspend fun searchMangaRequest(
        @Query("keyword") keyword: String,
        @Query("page") page: String
    ): Response<String>

    @Headers("Content-Type: text/html")
    @GET("tim-truyen/{genre}")
    suspend fun searchMangaByGenreRequest(
        @Path("genre") genre: String,
        @Query("page") page: String
    ): Response<String>
}