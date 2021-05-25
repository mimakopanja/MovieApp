package com.mirjanakopanja.movieapp.model.api

import com.mirjanakopanja.movieapp.BuildConfig
import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.data.Movies
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPI {

    @GET("movie/{id}")
    fun getMovieById(
        @Path("id") id: Long?,
        @Query("api_key") api_key: String = BuildConfig.api_key
    ): Call<MoviesDataTransfer>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") api_key: String = BuildConfig.api_key,
        @Query("page") page: Int? = 1
    ): Call<Movies>

    companion object {
        var BASE_URL = "https://api.themoviedb.org/3/"
        fun create() : MoviesAPI {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(MoviesAPI::class.java)
        }
    }
}