package com.example.tachiyomi_clone.provider

import com.example.tachiyomi_clone.data.model.Result
import com.example.tachiyomi_clone.data.model.mapper.ErrorDataMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

open class BaseProvider constructor(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val errorDataMapper: ErrorDataMapper? = null
) {

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T>? {
        return withContext(coroutineDispatcher) {
            try {
                Result.Success(apiCall.invoke())
            } catch (e: Throwable) {
                errorDataMapper?.transformExceptionToDomain(e)?.let { Result.Error(it) }
            }
        }
    }

}