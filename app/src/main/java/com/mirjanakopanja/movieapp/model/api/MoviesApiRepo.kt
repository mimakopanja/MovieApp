package com.mirjanakopanja.movieapp.model.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MoviesApiRepo {
     val api: MoviesAPI by lazy {
         val adapter = Retrofit.Builder()
             .baseUrl(ApiUtils.baseURL)
             .addConverterFactory(GsonConverterFactory.create())
             .client(ApiUtils.getOkHTTP())
             .build()

         adapter.create(MoviesAPI::class.java)
     }

}