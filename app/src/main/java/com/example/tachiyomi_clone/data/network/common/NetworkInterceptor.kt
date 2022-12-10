package com.example.tachiyomi_clone.data.network.common

import android.content.Context
import com.example.tachiyomi_clone.BuildConfig
import com.example.tachiyomi_clone.utils.Constant
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkInterceptor constructor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val aRequest = newRequest(request)
        return execute(chain, aRequest)
    }

    private fun newRequest(request: Request): Request {
        val builder = addHeader(request)
        return builder.build()
    }

    private fun addHeader(request: Request): Request.Builder {
        var builder = request.newBuilder()
//        builder = addUserAgentHeader(builder)
//        builder = addRefererHeader(builder)
        return builder
    }

    private fun addUserAgentHeader(builder: Request.Builder): Request.Builder {
        return builder.addHeader(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0"
        )
    }

    private fun addRefererHeader(builder: Request.Builder): Request.Builder {
        return builder.addHeader(
            "Referer",
            Constant.BASE_URL
        )
    }

    private fun execute(
        chain: Interceptor.Chain,
        request: Request
    ): Response {
        return chain.proceed(request)
    }
}