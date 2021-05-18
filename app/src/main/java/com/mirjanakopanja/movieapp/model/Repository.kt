package com.mirjanakopanja.movieapp.model

interface Repository {
    fun getMovieFromStorage(): List<Movies>
}