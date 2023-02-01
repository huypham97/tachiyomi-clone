package com.example.tachiyomi_clone.ui.common

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.FrameLayout

class WebtoonFrame(context: Context) : FrameLayout(context) {

    private val scaleDetector = ScaleGestureDetector(context, ScaleListener())

    private val recycler: WebtoonRecyclerView?
        get() = getChildAt(0) as? WebtoonRecyclerView

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            recycler?.onScale(scaleDetector.scaleFactor)
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
        }
    }
}