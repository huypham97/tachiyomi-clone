package com.example.tachiyomi_clone.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.text.SimpleDateFormat
import java.util.*


fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus ?: View(this)
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    kotlin.runCatching {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commitAllowingStateLoss()
    }
}

fun Response.asJsoup(html: String? = null): Document {
    return Jsoup.parse(html ?: body.string(), request.url.toString())
}

fun Boolean.toLong() = if (this) 1L else 0L

fun TextView.leftDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
}

fun Long.getDateTime(format: String): String {
    val sdf = SimpleDateFormat(format)
    val netDate = Date(this)
    return sdf.format(netDate)
}