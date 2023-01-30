package com.example.tachiyomi_clone.data.model.dto

import com.example.tachiyomi_clone.utils.asJsoup
import com.example.tachiyomi_clone.utils.imageOrNull
import okhttp3.Response
import org.jsoup.nodes.Element

data class MangasPageDto(val mangas: List<MangaDto>? = null, val hasNextPage: Boolean? = null) {

    companion object {

        private fun popularMangaSelector() = "div.items div.item"

        private fun popularMangaNextPageSelector() = "a.next-page, a[rel=next]"

        fun popularMangaParse(html: String, response: Response): MangasPageDto {
            val document = response.asJsoup(html = html)

            val mangas = document.select(popularMangaSelector()).map { element ->
                popularMangaFromElement(element)
            }

            val hasNextPage = popularMangaNextPageSelector().let { selector ->
                document.select(selector).first()
            } != null

            return MangasPageDto(mangas, hasNextPage)
        }

        fun popularMangaFromElement(element: Element): MangaDto {
            return MangaDto().apply {
                element.select("h3 a").let {
                    title = it.text()
                    setUrlWithoutDomain(it.attr("abs:href"))
                }
                thumbnail_url = element.select("div.image:first-of-type img").first()
                    ?.let { image -> imageOrNull(image) }
            }
        }
    }

}