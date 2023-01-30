package com.example.tachiyomi_clone.utils

import java.text.SimpleDateFormat
import java.util.*

val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

val currentYear by lazy { Calendar.getInstance(Locale.US)[1].toString().takeLast(2) }

fun String?.toDate(
    gmtOffset: String? = null
): Long {
    this ?: return 0

    val secondWords = listOf("second", "giây")
    val minuteWords = listOf("minute", "phút")
    val hourWords = listOf("hour", "giờ")
    val dayWords = listOf("day", "ngày")
    val agoWords = listOf("ago", "trước")

    return try {
        if (agoWords.any { this.contains(it, ignoreCase = true) }) {
            val trimmedDate = this.substringBefore(" ago").removeSuffix("s").split(" ")
            val calendar = Calendar.getInstance()

            when {
                dayWords.doesInclude(trimmedDate[1]) -> calendar.apply {
                    add(
                        Calendar.DAY_OF_MONTH,
                        -trimmedDate[0].toInt()
                    )
                }
                hourWords.doesInclude(trimmedDate[1]) -> calendar.apply {
                    add(
                        Calendar.HOUR_OF_DAY,
                        -trimmedDate[0].toInt()
                    )
                }
                minuteWords.doesInclude(trimmedDate[1]) -> calendar.apply {
                    add(
                        Calendar.MINUTE,
                        -trimmedDate[0].toInt()
                    )
                }
                secondWords.doesInclude(trimmedDate[1]) -> calendar.apply {
                    add(
                        Calendar.SECOND,
                        -trimmedDate[0].toInt()
                    )
                }
            }

            calendar.timeInMillis
        } else {
            (if (gmtOffset == null) this.substringAfterLast(" ") else "$this $gmtOffset").let {
                // timestamp has year
                if (Regex("""\d+/\d+/\d\d""").find(it)?.value != null) {
                    dateFormat.parse(it)?.time ?: 0
                } else {
                    // MangaSum - timestamp sometimes doesn't have year (current year implied)
                    dateFormat.parse("$it/$currentYear")?.time ?: 0
                }
            }
        }
    } catch (_: Exception) {
        0L
    }
}