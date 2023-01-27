package com.example.tachiyomi_clone.data.network.http

import com.example.tachiyomi_clone.data.model.dto.ChapterDto
import com.example.tachiyomi_clone.data.model.dto.MangaDto
import com.example.tachiyomi_clone.data.model.dto.MangasPageDto
import com.example.tachiyomi_clone.utils.Constant
import com.example.tachiyomi_clone.utils.asJsoup
import okhttp3.Request
import okhttp3.Response
import org.jsoup.nodes.Element
import java.net.URI
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*

class MangaSource {

    val baseUrl: String = Constant.BASE_URL

    val popularPath = "hot"

    val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

    val gmtOffset: String? = null

    val currentYear by lazy { Calendar.getInstance(Locale.US)[1].toString().takeLast(2) }

    fun popularMangaRequest(page: Int): Request {
        return GET("$baseUrl/$popularPath" + if (page > 1) "?page=$page" else "")
    }

    fun mangaDetailsRequest(mangaUrl: String): Request {
        return GET(baseUrl + mangaUrl)
    }

    fun chapterListRequest(mangaUrl: String): Request {
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

    fun chapterListParse(response: Response): List<ChapterDto> {
        val document = response.asJsoup()
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

    fun ChapterDto.setUrlWithoutDomain(url: String) {
        this.url = getUrlWithoutDomain(url)
    }

    private fun String?.toDate(): Long {
        this ?: return 0

        val secondWords = listOf("second", "giây")
        val minuteWords = listOf("minute", "phút")
        val hourWords = listOf("hour", "giờ")
        val dayWords = listOf("day", "ngày")
        val agoWords = listOf("ago", "trước")

        return try {
            if (agoWords.any { this.contains(it, ignoreCase = true) }) {
                val trimmedDate = this.substringBefore(" ago").removeSuffix("s").split(" ")
                val calendar = Calendar.getInstance()

                when {
                    dayWords.doesInclude(trimmedDate[1]) -> calendar.apply {
                        add(
                            Calendar.DAY_OF_MONTH,
                            -trimmedDate[0].toInt()
                        )
                    }
                    hourWords.doesInclude(trimmedDate[1]) -> calendar.apply {
                        add(
                            Calendar.HOUR_OF_DAY,
                            -trimmedDate[0].toInt()
                        )
                    }
                    minuteWords.doesInclude(trimmedDate[1]) -> calendar.apply {
                        add(
                            Calendar.MINUTE,
                            -trimmedDate[0].toInt()
                        )
                    }
                    secondWords.doesInclude(trimmedDate[1]) -> calendar.apply {
                        add(
                            Calendar.SECOND,
                            -trimmedDate[0].toInt()
                        )
                    }
                }

                calendar.timeInMillis
            } else {
                (if (gmtOffset == null) this.substringAfterLast(" ") else "$this $gmtOffset").let {
                    // timestamp has year
                    if (Regex("""\d+/\d+/\d\d""").find(it)?.value != null) {
                        dateFormat.parse(it)?.time ?: 0
                    } else {
                        // MangaSum - timestamp sometimes doesn't have year (current year implied)
                        dateFormat.parse("$it/$currentYear")?.time ?: 0
                    }
                }
            }
        } catch (_: Exception) {
            0L
        }
    }
}