package com.example.tachiyomi_clone.data.model.dto

import com.example.tachiyomi_clone.utils.asJsoup
import com.example.tachiyomi_clone.utils.imageOrNull
import okhttp3.Response

class PageDto(
    val index: Int,
    val url: String = "",
    val imageUrl: String? = null
) {

    companion object {

        private val pageListSelector = "div.page-chapter > img, li.blocks-gallery-item img"

        fun pageListParse(html: String?, response: Response): List<PageDto> {
            val document = response.asJsoup(html)
            return document.select(pageListSelector).mapNotNull { img -> imageOrNull(img) }
                .distinct()
                .mapIndexed { i, image -> PageDto(i, "", image) }
        }
    }



}