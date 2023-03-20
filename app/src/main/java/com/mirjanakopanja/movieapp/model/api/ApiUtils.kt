package com.mirjanakopanja.movieapp.model.api

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object ApiUtils {
    private val baseURLAddress = "https://api.themoviedb.org/"
    private val urlVersion = "3/"
    val baseURL = "$baseURLAddress$urlVersion"

    fun getOkHTTP(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            addInterceptor{ chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .method(original.method(), original.body())
                    .build()

                chain.proceed(request)
            }
        }
        return httpClient.build()
    }
}