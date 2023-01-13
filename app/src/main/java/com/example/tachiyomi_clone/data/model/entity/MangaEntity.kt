package com.example.tachiyomi_clone.data.model.entity

import com.example.tachiyomi_clone.data.model.dto.MangaDto
import com.example.tachiyomi_clone.data.model.dto.UpdateStrategy
import java.io.Serializable

data class MangaEntity(
    val id: Long,
    val favorite: Boolean,
    val lastUpdate: Long,
    val dateAdded: Long,
    val viewerFlags: Long,
    val chapterFlags: Long,
    val coverLastModified: Long,
    val url: String,
    val title: String,
    val artist: String?,
    val author: String?,
    val description: String?,
    val genre: List<String>?,
    val status: Long,
    val thumbnailUrl: String?,
    val updateStrategy: UpdateStrategy,
    val initialized: Boolean,
) : Serializable {

    companion object {
        fun create() = MangaEntity(
            id = -1L,
            url = "",
            title = "",
            favorite = false,
            lastUpdate = 0L,
            dateAdded = 0L,
            viewerFlags = 0L,
            chapterFlags = 0L,
            coverLastModified = 0L,
            artist = null,
            author = null,
            description = null,
            genre = null,
            status = 0L,
            thumbnailUrl = null,
            updateStrategy = UpdateStrategy.ALWAYS_UPDATE,
            initialized = false,
        )
    }
}

fun MangaDto.toDomainManga(): MangaEntity {
    return MangaEntity.create().copy(
        url = url ?: "",
        title = title ?: "",
        artist = artist,
        author = author,
        description = description,
        genre = getGenres(),
        status = status?.toLong() ?: 0L,
        thumbnailUrl = thumbnail_url,
        updateStrategy = update_strategy ?: UpdateStrategy.ALWAYS_UPDATE,
        initialized = initialized ?: false,
    )
}