package com.example.tachiyomi_clone.data.model.entity

import androidx.annotation.StringRes
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.data.model.dao.MangaDao
import com.example.tachiyomi_clone.data.model.dto.MangaDto
import com.example.tachiyomi_clone.data.model.dto.UpdateStrategy
import java.io.Serializable

data class MangaEntity(
    val id: Long,
    var favorite: Boolean,
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
    val genre: MutableList<GenreEntity>,
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
            genre = mutableListOf(),
            status = 0L,
            thumbnailUrl = null,
            updateStrategy = UpdateStrategy.ALWAYS_UPDATE,
            initialized = false,
        )
    }

    @StringRes
    fun getStatus(): Int {
        return when (status.toInt()) {
            MangaDto.UNKNOWN -> return R.string.unknown
            MangaDto.ONGOING -> return R.string.ongoing
            MangaDto.COMPLETED -> return R.string.completed
            MangaDto.LICENSED -> return R.string.licensed
            MangaDto.PUBLISHING_FINISHED -> R.string.publishing_finished
            MangaDto.CANCELLED -> R.string.cancelled
            MangaDto.ON_HIATUS -> R.string.on_hiatus
            else -> R.string.unknown
        }
    }
}

fun MangaDto.toDomain(): MangaEntity {
    return MangaEntity.create().copy(
        url = url ?: "",
        title = title ?: "",
        artist = artist,
        author = author,
        description = description,
        genre = genre.map { GenreEntity(it.title, it.pathUrl) }.toMutableList(),
        status = status?.toLong() ?: 0L,
        thumbnailUrl = thumbnail_url,
        updateStrategy = update_strategy ?: UpdateStrategy.ALWAYS_UPDATE,
        initialized = initialized ?: false,
    )
}

fun MangaDao.toDomain(): MangaEntity {
    return MangaEntity.create().copy(
        id = id ?: -1,
        url = url ?: "",
        title = title ?: "",
        artist = artist,
        author = author,
        description = description,
        status = status?.toLong() ?: 0L,
        thumbnailUrl = thumbnail_url,
        updateStrategy = update_strategy ?: UpdateStrategy.ALWAYS_UPDATE,
        initialized = initialized ?: false,
        favorite = favorite ?: false
    )
}