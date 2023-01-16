package com.example.tachiyomi_clone.data.network.http

import com.example.tachiyomi_clone.data.model.dto.MangaDto
import com.example.tachiyomi_clone.data.model.dto.MangasPageDto
import com.example.tachiyomi_clone.utils.Constant
import com.example.tachiyomi_clone.utils.asJsoup
import okhttp3.Request
import okhttp3.Response
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URI
import java.net.URISyntaxException

class MangaSource {

    val baseUrl: String = Constant.BASE_URL

    val popularPath = "hot"

    fun popularMangaRequest(page: Int): Request {
        return GET("$baseUrl/$popularPath" + if (page > 1) "?page=$page" else "")
    }

    fun mangaDetailsRequest(mangaUrl: String): Request {
        return GET(baseUrl + mangaUrl)
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

    fun mangaDetailsParse(response: Response): MangaDto {
        val document = response.asJsoup()
        return MangaDto.create().apply {
            document.select("article#item-detail").let { info ->
                author = info.select("li.author p.col-xs-8").text()
                status = info.select("li.status p.col-xs-8").text().toStatus()
                genre = info.select("li.kind p.col-xs-8 a").joinToString { it.text() }
                description = info.select("div.detail-content p").text()
                thumbnail_url = info.select("div.col-image img").first()?.let { imageOrNull(it) }
            }
        }
    }

    open fun String?.toStatus(): Int {
        val ongoingWords = listOf("Ongoing", "Updating", "Đang tiến hành")
        val completedWords = listOf("Complete", "Hoàn thành")
        return when {
            this == null -> MangaDto.UNKNOWN
            ongoingWords.doesInclude(this) -> MangaDto.ONGOING
            completedWords.doesInclude(this) -> MangaDto.COMPLETED
            else -> MangaDto.UNKNOWN
        }
    }

    private fun List<String>.doesInclude(thisWord: String): Boolean =
        this.any { it.contains(thisWord, ignoreCase = true) }
}