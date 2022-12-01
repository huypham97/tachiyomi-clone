package com.example.tachiyomi_clone.data.model.entity

import com.example.tachiyomi_clone.data.model.dto.UpdateStrategy

class MangaEntity {
    var url: String? = null
    var title: String? = null
    var artist: String? = null
    var author: String? = null
    var description: String? = null
    var genre: String? = null
    var status: Int? = null
    var thumbnail_url: String? = null
    var update_strategy: UpdateStrategy? = null
    var initialized: Boolean? = null
}