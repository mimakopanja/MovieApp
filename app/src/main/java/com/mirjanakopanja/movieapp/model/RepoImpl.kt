package com.mirjanakopanja.movieapp.model

class RepoImpl: Repository {

    override fun getMovieFromStorage() = getMovies()
}