package com.example.tachiyomi_clone.data.model.dto

import android.net.Uri
import com.example.tachiyomi_clone.utils.asJsoup
import com.example.tachiyomi_clone.utils.doesInclude
import com.example.tachiyomi_clone.utils.getUrlWithoutDomain
import com.example.tachiyomi_clone.utils.imageOrNull
import okhttp3.Response

data class MangaDto(
    var id: Long? = null,
    var url: String? = null,
    var title: String? = null,
    var artist: String? = null,
    var author: String? = null,
    var description: String? = null,
    var genre: MutableList<GenreDto> = mutableListOf(),
    var status: Int? = null,
    var thumbnail_url: String? = null,
    var update_strategy: UpdateStrategy? = null,
    var initialized: Boolean? = null,
    var favorite: Boolean? = null,
    var lastUpdate: Long? = null,
    var dateAdded: Long? = null,
    var viewerFlags: Long? = null,
    var chapterFlags: Long? = null,
    var coverLastModified: Long? = null,
) {

    companion object {
        const val UNKNOWN = 0
        const val ONGOING = 1
        const val COMPLETED = 2
        const val LICENSED = 3
        const val PUBLISHING_FINISHED = 4
        const val CANCELLED = 5
        const val ON_HIATUS = 6

        fun create() = MangaDto(
            status = UNKNOWN,
            update_strategy = UpdateStrategy.ALWAYS_UPDATE,
            initialized = false
        )

        fun mangaDetailsParse(html: String?, response: Response): MangaDto {
            val document = response.asJsoup(html)
            return create().apply {
                document.select("article#item-detail").let { info ->
                    author = info.select("li.author p.col-xs-8").text()
                    status = info.select("li.status p.col-xs-8").text().toStatus()
//                    genre = info.select("li.kind p.col-xs-8 a").joinToString { it.text() }
                    info.select("li.kind p.col-xs-8 a").map {
                        val segments = Uri.parse(it.attr("href")).path?.split("/")
                        genre.add(
                            GenreDto(
                                title = it.text(),
                                pathUrl = segments?.get(segments.size - 1)
                            )
                        )
                    }
                    description = info.select("div.detail-content p").text()
                    thumbnail_url =
                        info.select("div.col-image img").first()?.let { imageOrNull(it) }
                }
            }
        }
    }

    var listGenre: List<String>? = null

//    fun getGenres(): List<String>? {
//        if (genre.isNullOrBlank()) return null
//        return genre?.split(", ")?.map { it.trim() }?.filterNot { it.isBlank() }?.distinct()
//    }

    fun setUrlWithoutDomain(url: String) {
        this.url = getUrlWithoutDomain(url)
    }

    fun String?.toStatus(): Int {
        val ongoingWords = listOf("Ongoing", "Updating", "Đang tiến hành")
        val completedWords = listOf("Complete", "Hoàn thành")
        return when {
            this == null -> UNKNOWN
            ongoingWords.doesInclude(this) -> ONGOING
            completedWords.doesInclude(this) -> COMPLETED
            else -> UNKNOWN
        }
    }
}

/**
 * Define the update strategy for a single [MangaDto].
 * The strategy used will only take effect on the library update.
 */
enum class UpdateStrategy {
    /**
     * Series marked as always update will be included in the library
     * update if they aren't excluded by additional restrictions.
     */
    ALWAYS_UPDATE,

    /**
     * Series marked as only fetch once will be automatically skipped
     * during library updates. Useful for cases where the series is previously
     * known to be finished and have only a single chapter, for example.
     */
    ONLY_FETCH_ONCE,
}
