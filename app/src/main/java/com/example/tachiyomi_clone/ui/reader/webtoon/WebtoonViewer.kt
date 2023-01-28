package com.example.tachiyomi_clone.ui.reader.webtoon

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tachiyomi_clone.ui.reader.ReaderActivity

class WebtoonViewer(val activity: ReaderActivity) {

    val recycler = RecyclerView(activity)
    private val frame = FrameLayout(activity)
    private val adapter = WebtoonAdapter(this)

    init {
        initViews()
        setEventListeners()
    }

    private fun initViews() {
        recycler.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        recycler.isFocusable = false
        recycler.itemAnimator = null
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = adapter

        frame.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        frame.addView(recycler)
    }

    private fun setEventListeners() {
        activity.viewModel.isLoading.observe(activity) {
            if (!it) {
                adapter.updateItems(activity.viewModel.pageList)
            }
        }
    }

    fun getView(): View {
        return frame
    }
}