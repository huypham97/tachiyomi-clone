package com.example.tachiyomi_clone.di.module

import android.app.Application
import android.content.Context
import com.example.tachiyomi_clone.BuildConfig
import com.example.tachiyomi_clone.data.network.common.NetworkInterceptor
import com.example.tachiyomi_clone.data.network.http.MangaSource
import com.example.tachiyomi_clone.data.network.service.HomeService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

@Module
@Suppress("UNUSED")
class NetworkModule(baseUrl: String) {

    private var baseAuthUrl = baseUrl

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    @Provides
    fun provideCache(application: Application): Cache {
        val cacheSize = 1024 * 1024 * 10L
        return Cache(application.cacheDir, cacheSize)
    }

    @Provides
    fun provideNetworkInterceptor(context: Context): NetworkInterceptor =
        NetworkInterceptor(context)

    @Provides
    fun provideOkHttpClient(
        context: Context,
        cache: Cache,
        networkInterceptor: NetworkInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        val builder = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .cache(cache)
            .addInterceptor(networkInterceptor)
            .addInterceptor(loggingInterceptor)
        return builder.build()
    }

    @Provides
    fun provideRetrofit(httpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(this.baseAuthUrl)
            .build()
    }

    @Provides
    fun provideHomeService(retrofit: Retrofit): HomeService =
        retrofit.create(HomeService::class.java)

    @Provides
    fun provideMangaSource(): MangaSource {
        return MangaSource()
    }
}