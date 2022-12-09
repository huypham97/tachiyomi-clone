package com.example.tachiyomi_clone.data.model.mapper

import com.example.tachiyomi_clone.data.model.dto.MangasPageDto
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import javax.inject.Inject

class MangasPageMapper @Inject constructor(private val mangaMapper: MangaMapper) :
    BaseMapper<MangasPageDto, MangasPageEntity> {

    override fun toEntity(dto: MangasPageDto): MangasPageEntity = MangasPageEntity().apply {
        mangas = dto.mangas?.map {
            mangaMapper.toEntity(it)
        }
        hasNextPage = dto.hasNextPage
    }

    override fun fromEntity(entity: MangasPageEntity): MangasPageDto {
        return MangasPageDto(entity.mangas?.map {
            mangaMapper.fromEntity(it)
        }, entity.hasNextPage)
    }
}