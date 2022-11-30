package com.example.tachiyomi_clone.data.model.dto

import java.io.IOException

enum class Type {
    NO_CONNECTION,
    HTTP_ERROR,
    GENERIC_ERROR
}

data class ErrorEntity constructor(
    var type: Type,
    var errorCode: Int = 0,
    var errorMessage: String = ""
) : IOException()