package com.example.tachiyomi_clone.data.network.http

import com.example.tachiyomi_clone.BuildConfig
import com.example.tachiyomi_clone.data.model.dto.MangaDto
import com.example.tachiyomi_clone.data.model.dto.MangasPageDto
import com.example.tachiyomi_clone.utils.asJsoup
import okhttp3.Request
import okhttp3.Response
import org.jsoup.nodes.Element
import java.net.URI
import java.net.URISyntaxException

class MangaSource {

    val baseUrl: String = BuildConfig.BASE_URL

    val popularPath = "hot"

    fun popularMangaRequest(page: Int): Request {
        return GET("$baseUrl/$popularPath" + if (page > 1) "?page=$page" else "")
    }

    fun popularMangaParse(response: Response): MangasPageDto {
        val document = response.asJsoup()

        val mangas = document.select(popularMangaSelector()).map { element ->
            popularMangaFromElement(element)
        }

        val hasNextPage = popularMangaNextPageSelector().let { selector ->
            document.select(selector).first()
        } != null

        return MangasPageDto(mangas, hasNextPage)
    }

    fun popularMangaSelector() = "div.items div.item"

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

    fun MangaDto.setUrlWithoutDomain(url: String) {
        this.url = getUrlWithoutDomain(url)
    }

    private fun getUrlWithoutDomain(orig: String): String {
        return try {
            val uri = URI(orig.replace(" ", "%20"))
            var out = uri.path
            if (uri.query != null) {
                out += "?" + uri.query
            }
            if (uri.fragment != null) {
                out += "#" + uri.fragment
            }
            out
        } catch (e: URISyntaxException) {
            orig
        }
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

    fun popularMangaNextPageSelector() = "a.next-page, a[rel=next]"

}