package com.example.tachiyomi_clone.data.model.dto

import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import java.io.Serializable

class ChapterDto : Serializable {
    var url: String? = null

    var name: String? = null

    var date_upload: Long = 0

    var chapter_number: Float = -1f

    var scanlator: String? = null

    var id: Long? = null

    var manga_id: Long? = null

    var read: Boolean = false

    var bookmark: Boolean = false

    var last_page_read: Int = 0

    var date_fetch: Long = 0

    var source_order: Int = 0
}

fun ChapterDto.toDomain(): ChapterEntity? {
    if (id == null || manga_id == null) return null
    return ChapterEntity(
        id = id!!,
        mangaId = manga_id!!,
        read = read,
        bookmark = bookmark,
        lastPageRead = last_page_read.toLong(),
        dateFetch = date_fetch,
        sourceOrder = source_order.toLong(),
        url = url ?: "",
        name = name ?: "",
        dateUpload = date_upload,
        chapterNumber = chapter_number,
        scanlator = scanlator,
    )
}