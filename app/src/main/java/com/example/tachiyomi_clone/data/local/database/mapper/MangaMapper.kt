package com.example.tachiyomi_clone.data.local.database.mapper

import com.example.tachiyomi_clone.data.model.dto.UpdateStrategy
import com.example.tachiyomi_clone.data.model.entity.MangaEntity

val mangaMapper: (Long, String, String?, String?, String?, List<String>?, String, Long, String?, Boolean, Long?, Long?, Boolean, Long, Long, Long, Long, UpdateStrategy) -> MangaEntity =
    { id, url, artist, author, description, genre, title, status, thumbnailUrl, favorite, lastUpdate, _, initialized, viewerFlags, chapterFlags, coverLastModified, dateAdded, updateStrategy ->
        MangaEntity(
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
            genre = mutableListOf(),
            status = status,
            thumbnailUrl = thumbnailUrl,
            updateStrategy = updateStrategy,
            initialized = initialized,
        )
    }