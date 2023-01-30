package com.example.tachiyomi_clone.data.network.http

import com.example.tachiyomi_clone.data.model.dto.PageDto
import com.example.tachiyomi_clone.utils.Constant
import com.example.tachiyomi_clone.utils.asJsoup
import okhttp3.Headers
import okhttp3.Request
import okhttp3.Response
import org.jsoup.nodes.Element

class MangaSource {

    val baseUrl: String = Constant.BASE_URL

    val headers: Headers by lazy { headersBuilder().build() }

    private fun headersBuilder() = Headers.Builder().apply {
        add("Referer", baseUrl)
    }

    fun imageRequest(imageUrl: String): Request {
        return GET(imageUrl, headers)
    }

    fun imageOrNull(element: Element): String? {
        fun Element.hasValidAttr(attr: String): Boolean {
            val regex = Regex("""https?://.*""", RegexOption.IGNORE_CASE)
            return when {
                this.attr(attr).isNullOrBlank() -> false
                this.attr("abs:$attr").matches(regex) -> true
                else -> false
            }
        }

        return when {
            element.hasValidAttr("data-original") -> element.attr("abs:data-original")
            element.hasValidAttr("data-src") -> element.attr("abs:data-src")
            element.hasValidAttr("src") -> element.attr("abs:src")
            else -> null
        }
    }

    val pageListSelector = "div.page-chapter > img, li.blocks-gallery-item img"

    fun pageListParse(html: String?, response: Response): List<PageDto> {
        val document = response.asJsoup(html)
        return document.select(pageListSelector).mapNotNull { img -> imageOrNull(img) }
            .distinct()
            .mapIndexed { i, image -> PageDto(i, "", image) }
    }
}