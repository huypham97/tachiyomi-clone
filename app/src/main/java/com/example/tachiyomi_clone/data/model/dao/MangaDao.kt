package com.example.tachiyomi_clone.data.model.dao

import com.example.tachiyomi_clone.data.model.dto.UpdateStrategy

data class MangaDao(
    var id: Long? = null,
    var url: String? = null,
    var title: String? = null,
    var artist: String? = null,
    var author: String? = null,
    var description: String? = null,
    var status: Int? = null,
    var thumbnail_url: String? = null,
    var update_strategy: UpdateStrategy? = null,
    var initialized: Boolean? = null,
    var favorite: Boolean? = null,
    var lastUpdate: Long? = null,
    var dateAdded: Long? = null,
    var viewerFlags: Long? = null,
    var chapterFlags: Long? = null,
    var coverLastModified: Long? = null,
)