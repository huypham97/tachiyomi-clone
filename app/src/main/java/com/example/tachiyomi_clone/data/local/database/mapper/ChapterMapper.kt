package com.example.tachiyomi_clone.data.local.database.mapper

import com.example.tachiyomi_clone.data.model.entity.ChapterEntity

val chapterMapper: (Long, Long, String, String, String?, Boolean, Boolean, Long, Float, Long, Long, Long) -> ChapterEntity =
    { id, mangaId, url, name, scanlator, read, bookmark, lastPageRead, chapterNumber, sourceOrder, dateFetch, dateUpload ->
        ChapterEntity(
            id = id,
            mangaId = mangaId,
            read = read,
            bookmark = bookmark,
            lastPageRead = lastPageRead,
            dateFetch = dateFetch,
            sourceOrder = sourceOrder,
            url = url,
            name = name,
            dateUpload = dateUpload,
            chapterNumber = chapterNumber,
            scanlator = scanlator,
        )
    }
