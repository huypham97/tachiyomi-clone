package com.example.tachiyomi_clone.data.repository

import com.example.tachiyomi_clone.data.model.entity.MangaEntity

interface MangaRepository {

    suspend fun getMangaByUrl(url: String): MangaEntity?

    suspend fun insert(mangaEntity: MangaEntity): Long?
}