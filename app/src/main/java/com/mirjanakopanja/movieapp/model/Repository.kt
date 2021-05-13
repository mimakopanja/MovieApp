package com.mirjanakopanja.movieapp.model

interface Repository {
    fun getMoviesFromServer(id: Int): Movies
    fun getMovieFromStorage(): List<Movies>
}