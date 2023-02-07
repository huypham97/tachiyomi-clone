package com.example.tachiyomi_clone.data.model.entity

import java.io.Serializable

class MangasPageEntity: Serializable {
    var mangas: List<MangaEntity>? = null
    var hasNextPage: Boolean? = null
    var title: String? = null
    var type: MODULE_TYPE? = null
}

enum class MODULE_TYPE {
    NEWEST, POPULAR, BOY, GIRL
}