package com.example.tachiyomi_clone.common.event

open class OneTimeEvent<out T>(private val content: T) : BaseEvent<T>() {
    private var hasBeenHandled = false

    /**
     * Returns the content and prevents its use again.
     */
    override fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}

abstract class BaseEvent<out T>() {
    abstract fun getContentIfNotHandled(): T?
}