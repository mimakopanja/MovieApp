package com.mirjanakopanja.movieapp.model.api

import com.mirjanakopanja.movieapp.BuildConfig
import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.data.Movies
import com.mirjanakopanja.movieapp.data.cast.Actor
import com.mirjanakopanja.movieapp.data.cast.CastCredits
import com.mirjanakopanja.movieapp.data.tv.ShowDataTransfer
import com.mirjanakopanja.movieapp.data.tv.TvShows
import com.mirjanakopanja.movieapp.data.video.VideoResponse
import retrofit2.Call
import retrofit2.Response
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

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") api_key: String = BuildConfig.api_key,
        @Query("page") page: Int? = 1
    ): Call<Movies>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") api_key: String = BuildConfig.api_key,
        @Query("page") page: Int? = 1
    ): Call<Movies>

    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(
        @Path("movie_id") movie_id: Long?,
        @Query ("api_key") apiKey : String? = BuildConfig.api_key
    ): Call<CastCredits>

    @GET("person/{actor_id}")
    fun getActor(
        @Path("actor_id") actor_id: Int?,
        @Query ("api_key") apiKey: String
    ): Call<Actor>

    @GET("search/movie")
    fun searchMovies(
        @Query("api_key") api_key: String? = BuildConfig.api_key,
        @Query("query") query: String
    ): Call<Movies>

    @GET("tv/{id}")
    fun getTvShowById(
        @Path("id") id: Long?,
        @Query("api_key") api_key: String? = BuildConfig.api_key
    ): Call<ShowDataTransfer>

    @GET("tv/popular")
    fun getPopularTvShows(
        @Query("api_key") api_key: String = BuildConfig.api_key,
        @Query("page") page: Int? = 1
    ): Call<TvShows>

    @GET("movie/{movie_id}/videos")
    fun getVideos(
        @Path("movie_id") movie_id: Long?,
        @Query ("api_key") apiKey : String? = BuildConfig.api_key
    ): Call<VideoResponse>

    @GET("tv/{show_id}/videos")
    fun getShowTrailer(
        @Path("show_id") show_id: Long?,
        @Query ("api_key") apiKey : String? = BuildConfig.api_key
    ): Call<VideoResponse>

    companion object {
        private var BASE_URL = "https://api.themoviedb.org/3/"
        fun create() : MoviesAPI {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(MoviesAPI::class.java)
        }
    }
}