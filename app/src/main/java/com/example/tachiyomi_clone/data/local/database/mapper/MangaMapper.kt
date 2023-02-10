package com.example.tachiyomi_clone.data.local.database.mapper

import com.example.tachiyomi_clone.data.model.dao.MangaDao
import com.example.tachiyomi_clone.data.model.dto.UpdateStrategy

val mangaMapper: (Long, String, String?, String?, String?, String, Long, String?, Boolean, Long?, Long?, Boolean, Long, Long, Long, Long, UpdateStrategy) -> MangaDao =
    { id, url, artist, author, description, title, status, thumbnailUrl, favorite, lastUpdate, _, initialized, viewerFlags, chapterFlags, coverLastModified, dateAdded, updateStrategy ->
        MangaDao(
            id = id,
            favorite = favorite,
            lastUpdate = lastUpdate ?: 0,
            dateAdded = dateAdded,
            viewerFlags = viewerFlags,
            chapterFlags = chapterFlags,
            coverLastModified = coverLastModified,
            url = url,
            title = title,
            artist = artist,
            author = author,
            description = description,
            status = status.toInt(),
            thumbnail_url = thumbnailUrl,
            update_strategy = updateStrategy,
            initialized = initialized,
        )
    }