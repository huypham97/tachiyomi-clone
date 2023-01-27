package com.example.tachiyomi_clone.utils

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

object Common {

    fun handleGlideUrl(url: String): GlideUrl =
        GlideUrl(url, LazyHeaders.Builder().addHeader("Referer", Constant.BASE_URL).build())

}
