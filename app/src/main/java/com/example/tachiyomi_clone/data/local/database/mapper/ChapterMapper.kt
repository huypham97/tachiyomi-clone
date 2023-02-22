package com.example.tachiyomi_clone.data.local.database.mapper

import com.example.tachiyomi_clone.data.model.entity.ChapterEntity

val chapterMapper: (Long, Long, String, String, Boolean, Long, Long) -> ChapterEntity =
    { id, mangaId, url, name, read, sourceOrder, dateUpload ->
        ChapterEntity(
            id = id,
            mangaId = mangaId,
            read = read,
            sourceOrder = sourceOrder,
            url = url,
            name = name,
            dateUpload = dateUpload,
            null, null
        )
    }
