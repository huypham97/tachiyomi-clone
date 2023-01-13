package com.example.tachiyomi_clone.data.model.mapper

import com.example.tachiyomi_clone.data.local.database.listOfStringsAdapter
import com.example.tachiyomi_clone.data.model.dto.MangaDto
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.model.entity.toDomainManga
import javax.inject.Inject

class MangaMapper @Inject constructor() : BaseMapper<MangaDto, MangaEntity> {
    override fun toEntity(dto: MangaDto): MangaEntity = dto.toDomainManga()

    override fun fromEntity(entity: MangaEntity): MangaDto {
        return MangaDto(
            url = entity.url,
            title = entity.title,
            artist = entity.artist,
            author = entity.author,
            description = entity.description,
            genre = entity.genre?.let(listOfStringsAdapter::encode),
            status = entity.status?.toInt(),
            thumbnail_url = entity.thumbnailUrl,
            update_strategy = entity.updateStrategy,
            initialized = entity.initialized,
        )
    }
}