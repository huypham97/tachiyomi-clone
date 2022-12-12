package com.example.tachiyomi_clone.data.network.repository

import com.example.tachiyomi_clone.data.model.entity.MangaPagingSourceType

interface HomeRepository {
    fun fetchPopularManga(): MangaPagingSourceType
}