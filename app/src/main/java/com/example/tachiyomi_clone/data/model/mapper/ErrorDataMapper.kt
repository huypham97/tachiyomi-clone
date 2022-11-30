package com.example.tachiyomi_clone.data.model.mapper

import com.example.netflixclone.data.network.common.BaseErrorResponse
import com.example.tachiyomi_clone.data.model.dto.ErrorEntity
import com.example.tachiyomi_clone.data.model.dto.Type
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Retrofit
import javax.inject.Inject

class ErrorDataMapper @Inject constructor(private val retrofit: Retrofit) {

    fun transformExceptionToDomain(throwable: Throwable): ErrorEntity {
        return when (throwable) {
            is ErrorEntity -> throwable
            is HttpException -> transformToDomain(throwable)
            else -> ErrorEntity(Type.GENERIC_ERROR)
        }
    }

    private fun transformToDomain(httpException: HttpException): ErrorEntity {
        val converter: Converter<ResponseBody, BaseErrorResponse> = retrofit.responseBodyConverter(
            BaseErrorResponse::class.java,
            arrayOfNulls<Annotation>(0)
        )
        lateinit var baseErrorResponse: BaseErrorResponse
        val htmlBody = httpException.response()?.errorBody()
        try {
            if (htmlBody != null) {
                baseErrorResponse = converter.convert(htmlBody)!!
            }
        } catch (exception: Exception) {
            return ErrorEntity(Type.GENERIC_ERROR)
        }

        val errorCode = baseErrorResponse.code ?: 0
        val errorMessage = baseErrorResponse.message ?: ""

        return ErrorEntity(Type.HTTP_ERROR, errorCode, errorMessage)
    }

}