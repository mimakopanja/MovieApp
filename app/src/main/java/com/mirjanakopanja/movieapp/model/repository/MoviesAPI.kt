package com.mirjanakopanja.movieapp.model.repository

import com.mirjanakopanja.movieapp.BuildConfig
import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.model.Movies
import com.mirjanakopanja.movieapp.popular.MovieResult
import com.mirjanakopanja.movieapp.popular.Result
import retrofit2.Call
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
    fun getPopularList(
        @Query("api_key") api_key: String = BuildConfig.api_key,
        @Query("page") page: Int = 1
    ): Call<MoviesDataTransfer?>

    @GET("movie/popular")
        fun getPopularMovies(
            @Query("api_key") api_key: String = BuildConfig.api_key,
            @Query("page") page: Int = 1
        ) : Call<MoviesDataTransfer?>

}