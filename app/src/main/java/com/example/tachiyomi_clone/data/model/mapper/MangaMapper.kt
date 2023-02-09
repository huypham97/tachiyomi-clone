package com.example.tachiyomi_clone.data.model.mapper

import com.example.tachiyomi_clone.data.model.dto.GenreDto
import com.example.tachiyomi_clone.data.model.dto.MangaDto
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.model.entity.toDomain
import javax.inject.Inject

class MangaMapper @Inject constructor() : BaseMapper<MangaDto, MangaEntity> {
    override fun toEntity(dto: MangaDto): MangaEntity = dto.toDomain()

    override fun fromEntity(entity: MangaEntity): MangaDto {
        return MangaDto(
            url = entity.url,
            title = entity.title,
            artist = entity.artist,
            author = entity.author,
            description = entity.description,
            genre = entity.genre.map { GenreDto(it.title, it.pathUrl) }.toMutableList(),
            status = entity.status.toInt(),
            thumbnail_url = entity.thumbnailUrl,
            update_strategy = entity.updateStrategy,
            initialized = entity.initialized,
        )
    }
}