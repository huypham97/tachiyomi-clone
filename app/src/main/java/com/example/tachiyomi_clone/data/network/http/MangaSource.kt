package com.example.tachiyomi_clone.data.network.http

import com.example.tachiyomi_clone.BuildConfig
import okhttp3.Request

class MangaSource {

    val baseUrl: String = BuildConfig.BASE_URL

    val popularPath = "hot"

    fun popularMangaRequest(page: Int): Request {
        return GET("$baseUrl/$popularPath" + if (page > 1) "?page=$page" else "")
    }

}