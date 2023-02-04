package com.example.tachiyomi_clone.utils.system

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.Window

object ScreenUtils {

    fun applyWindow(window: Window) {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
    }

    fun getDp(context: Context, `val`: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (`val` * scale + 0.5f).toInt()
    }

    fun getStatusBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
    }

    fun getAppBarHeight(context: Context): Int {
        val tv = TypedValue()
        return if (context.theme.resolveAttribute(
                android.R.attr.actionBarSize,
                tv,
                true
            )
        ) TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics) else 0
    }
}