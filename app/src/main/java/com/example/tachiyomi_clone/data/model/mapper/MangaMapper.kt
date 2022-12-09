package com.example.tachiyomi_clone.data.model.mapper

import com.example.tachiyomi_clone.data.model.dto.MangaDto
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import javax.inject.Inject

class MangaMapper @Inject constructor() : BaseMapper<MangaDto, MangaEntity> {
    override fun toEntity(dto: MangaDto): MangaEntity = MangaEntity().apply {
        url = dto.url
        title = dto.title
        artist = dto.artist
        author = dto.author
        description = dto.description
        genre = dto.genre
        status = dto.status
        thumbnail_url = dto.thumbnail_url
        update_strategy = dto.update_strategy
        initialized = dto.initialized
    }

    override fun fromEntity(entity: MangaEntity): MangaDto {
        return MangaDto().apply {
            url = entity.url
            title = entity.title
            artist = entity.artist
            author = entity.author
            description = entity.description
            genre = entity.genre
            status = entity.status
            thumbnail_url = entity.thumbnail_url
            update_strategy = entity.update_strategy
            initialized = entity.initialized
        }

    }
}