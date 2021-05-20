package com.mirjanakopanja.movieapp.model.repository

import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.model.Movies
import com.mirjanakopanja.movieapp.popular.Result
import retrofit2.Call
import retrofit2.Callback

interface Repository {
    fun getMoviesFromServerAsync(id: Long?, callback: Callback<MoviesDataTransfer>)
    fun getMoviesFromServer(id: Long?): Movies
    fun getMovieFromStorage(): List<Movies>
    fun getMovieListFromSite(): List<MoviesDataTransfer?>
}