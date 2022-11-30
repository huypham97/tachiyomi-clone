package com.example.tachiyomi_clone.utils

import android.graphics.Color
import android.view.View
import android.view.Window

object ScreenUtils {

    fun applyWindow(window: Window) {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
    }

}