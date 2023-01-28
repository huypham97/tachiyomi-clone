package com.example.tachiyomi_clone.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun byteArrayToBitmap(data: ByteArray): Bitmap? {
    return BitmapFactory.decodeByteArray(data, 0, data.size)
}