package com.mirjanakopanja.movieapp.model

interface Repository {
    fun getMoviesFromServer(): Movies
    fun getMovieFromStorage(): List<Movies>
}