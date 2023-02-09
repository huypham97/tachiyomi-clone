package com.example.tachiyomi_clone.ui.reader.webtoon

import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tachiyomi_clone.ui.reader.ReaderFragment
import com.example.tachiyomi_clone.utils.Constant

class WebtoonViewer(val fragment: ReaderFragment) {

    val recycler = WebtoonRecyclerView(fragment.requireContext())
    private val frame = WebtoonFrame(fragment.requireContext())
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
        recycler.layoutManager = LinearLayoutManager(fragment.context)
        recycler.adapter = adapter
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if ((dy > Constant.SCROLL_THRESHOLD || dy < -Constant.SCROLL_THRESHOLD) && fragment.menuVisible) {
                    fragment.hideMenu()
                }
                if (!recycler.canScrollVertically(1) && dy > 0) {
                    fragment.showMenu()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        recycler.tapListener = {
            fragment.toggleMenu()
        }

        frame.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        frame.addView(recycler)
    }

    private fun setEventListeners() {
        fragment.viewModel.isLoading.observe(fragment) {
            if (!it) {
                adapter.updateItems(fragment.viewModel.pageList)
                if (fragment.viewModel.pageList.size > 0)
                    recycler.scrollToPosition(0)
            }
        }
    }

    fun getView(): View {
        return frame
    }

    fun handleKeyEvent(event: KeyEvent): Boolean {
        val isUp = event.action == KeyEvent.ACTION_UP

        when (event.keyCode) {
            KeyEvent.KEYCODE_MENU -> if (isUp) fragment.toggleMenu()
            else -> return false
        }
        return true
    }
}