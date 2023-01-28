package com.example.tachiyomi_clone.data.model.entity

class PageEntity(
    val index: Int,
    val url: String = "",
    val imageUrl: String? = null,
    var byte: ByteArray? = null
)