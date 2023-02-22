package com.example.tachiyomi_clone.data.model.mapper

import com.example.tachiyomi_clone.data.model.dto.ChapterDto
import com.example.tachiyomi_clone.data.model.dto.toDomain
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import javax.inject.Inject

class ChapterMapper @Inject constructor() : BaseMapper<ChapterDto, ChapterEntity> {
    override fun toEntity(dto: ChapterDto): ChapterEntity = dto.toDomain() ?: ChapterEntity.create()

    override fun fromEntity(entity: ChapterEntity): ChapterDto {
        return ChapterDto().apply {
            url = entity.url
            name = entity.name
            date_upload = entity.dateUpload
            id = entity.id
            manga_id = entity.mangaId
            read = entity.read
            source_order = entity.sourceOrder.toInt()
        }
    }
}