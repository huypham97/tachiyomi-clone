package com.example.tachiyomi_clone.data.model.dto

import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.utils.asJsoup
import com.example.tachiyomi_clone.utils.getUrlWithoutDomain
import com.example.tachiyomi_clone.utils.toDate
import okhttp3.Response
import org.jsoup.nodes.Element
import java.io.Serializable

class ChapterDto : Serializable {
    var url: String? = null

    var name: String? = null

    var date_upload: Long = 0

    var id: Long? = null

    var manga_id: Long? = null

    var read: Boolean = false

    var source_order: Int = 0

    companion object {

        fun chapterListParse(html: String?, response: Response): List<ChapterDto> {
            val document = response.asJsoup(html)
            return document.select(chapterListSelector()).map { chapterFromElement(it) }
        }

        fun chapterListSelector() = "div.list-chapter li.row:not(.heading)"

        fun chapterFromElement(element: Element): ChapterDto {
            return ChapterDto().apply {
                element.select("a").let {
                    name = it.text()
                    setUrlWithoutDomain(it.attr("href"))
                }
                date_upload = element.select("div.col-xs-4").text().toDate()
            }
        }
    }

    fun setUrlWithoutDomain(url: String) {
        this.url = getUrlWithoutDomain(url)
    }
}

fun ChapterDto.toDomain(): ChapterEntity? {
    if (id == null || manga_id == null) return null
    return ChapterEntity(
        id = id!!,
        mangaId = manga_id!!,
        read = read,
        sourceOrder = source_order.toLong(),
        url = url ?: "",
        name = name ?: "",
        dateUpload = date_upload,
        null, null
    )
}