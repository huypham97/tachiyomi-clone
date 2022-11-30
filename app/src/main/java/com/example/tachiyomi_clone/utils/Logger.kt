package com.example.tachiyomi_clone.utils

import android.util.Log
import com.example.tachiyomi_clone.BuildConfig

object Logger {
    private const val GLOBAL_TAG = "LOGGER"

    /**
     * Log debug
     */
    fun d(tag: String? = GLOBAL_TAG, debugString: String?) {
        if (!BuildConfig.DEBUG)
            return
        debugString?.let {
            Log.d(tag, it)
        }
    }

    /**
     * Log error
     */
    fun e(tag: String? = GLOBAL_TAG, errorString: String?) {
        if (!BuildConfig.DEBUG)
            return
        errorString?.let {
            Log.e(tag, it)
        }
    }

    /**
     * Log info
     */
    fun i(tag: String? = GLOBAL_TAG, infoString: String?) {
        if (!BuildConfig.DEBUG)
            return
        infoString?.let {
            Log.e(tag, it)
        }
    }

    /**
     * Log exception with log e
     * @see Logger.e
     */
    fun logException(tag: String? = GLOBAL_TAG, exception: Exception?) {
        if (!BuildConfig.DEBUG)
            return
        e(null, exception?.message)
    }

    /**
     * Log throwable with log e
     * @see Logger.e
     */
    fun logException(tag: String? = GLOBAL_TAG, throwable: Throwable?) {
        if (!BuildConfig.DEBUG)
            return
        e(null, throwable?.message)
    }
}