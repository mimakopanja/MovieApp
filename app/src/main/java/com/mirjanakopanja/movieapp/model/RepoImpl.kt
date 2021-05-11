package com.mirjanakopanja.movieapp.model

class RepoImpl: Repository {

 /*   override fun getMoviesFromServer(): Movies {
//        return Movies()
    }*/

    override fun getMovieFromStorage(): List<Movies> {
        return getMovies()
    }
}