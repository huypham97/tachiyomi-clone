package com.example.tachiyomi_clone.data.model.mapper

import com.example.tachiyomi_clone.data.model.dto.PageDto
import com.example.tachiyomi_clone.data.model.entity.PageEntity
import javax.inject.Inject

class PageMapper @Inject constructor() : BaseMapper<PageDto, PageEntity> {
    override fun toEntity(dto: PageDto): PageEntity {
        return PageEntity(index = dto.index, url = dto.url, imageUrl = dto.imageUrl)
    }

    override fun fromEntity(entity: PageEntity): PageDto {
        return PageDto(index = entity.index, url = entity.url, imageUrl = entity.imageUrl)
    }
}