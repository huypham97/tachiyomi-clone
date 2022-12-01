package com.example.tachiyomi_clone.data.model.dto

class MangaDto {
    var url: String? = null
    var title: String? = null
    var artist: String? = null
    var author: String? = null
    var description: String? = null
    var genre: String? = null
    var status: Int? = null
    var thumbnail_url: String? = null
    var update_strategy: UpdateStrategy? = null
    var initialized: Boolean? = null
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
