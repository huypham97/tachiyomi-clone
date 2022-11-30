package com.example.netflixclone.data.network.common

import com.google.gson.annotations.SerializedName

data class BaseErrorResponse(
    @SerializedName(value = "success") val success: Boolean?,
    @SerializedName(value = "status_code") val code: Int?,
    @SerializedName(value = "status_message") val message: String?
)