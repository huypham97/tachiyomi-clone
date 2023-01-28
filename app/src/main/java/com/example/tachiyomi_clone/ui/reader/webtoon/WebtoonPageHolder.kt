package com.example.tachiyomi_clone.ui.reader.webtoon

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tachiyomi_clone.data.model.entity.PageEntity

class WebtoonPageHolder(private val frame: ReaderPageImageView, private val viewer: WebtoonViewer) :
    RecyclerView.ViewHolder(frame) {

    val context: Context get() = itemView.context

    private var page: PageEntity? = null

    init {
        refreshLayoutParams()
    }

    fun recycle() {
        frame.recycle()
    }

    fun bind(page: PageEntity) {
        this.page = page
        setImage()
        refreshLayoutParams()
    }

    private fun setImage() {
        page?.byte?.let { frame.setImage(it) }
    }

    private fun refreshLayoutParams() {
        frame.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            marginEnd = 0
            marginStart = 0
        }
    }
}