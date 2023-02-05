package com.example.tachiyomi_clone.ui.reader.webtoon

import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tachiyomi_clone.ui.reader.ReaderActivity
import com.example.tachiyomi_clone.utils.Constant

class WebtoonViewer(val activity: ReaderActivity) {

    val recycler = WebtoonRecyclerView(activity)
    private val frame = WebtoonFrame(activity)
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
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if ((dy > Constant.SCROLL_THRESHOLD || dy < -Constant.SCROLL_THRESHOLD) && activity.menuVisible) {
                    activity.hideMenu()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        recycler.tapListener = {
            activity.toggleMenu()
        }

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

    fun handleKeyEvent(event: KeyEvent): Boolean {
        val isUp = event.action == KeyEvent.ACTION_UP

        when (event.keyCode) {
            KeyEvent.KEYCODE_MENU -> if (isUp) activity.toggleMenu()
            else -> return false
        }
        return true
    }
}