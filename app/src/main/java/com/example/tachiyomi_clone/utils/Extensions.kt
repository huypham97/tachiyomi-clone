package com.example.tachiyomi_clone.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import okhttp3.Response
import okhttp3.ResponseBody
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
    return Jsoup.parse(html ?: body.toString(), request.url.toString())
}

fun ResponseBody.asJsoup(html: String? = null): Document {
    return Jsoup.parse(html ?: string())
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

fun List<String>.doesInclude(thisWord: String): Boolean =
    this.any { it.contains(thisWord, ignoreCase = true) }

fun String.loadByHtml(): Spanned? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(
            this,
            Html.FROM_HTML_MODE_COMPACT
        )
    } else {
        Html.fromHtml(this)
    }
}

fun String.setColor(context: Context, @ColorRes colorRes: Int): String {
    return "<font color='${ContextCompat.getColor(context, colorRes)}'>$this</font>"
}

val Context.animatorDurationScale: Float
    get() = Settings.Global.getFloat(this.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f)