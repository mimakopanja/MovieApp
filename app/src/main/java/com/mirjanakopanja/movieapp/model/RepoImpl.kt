package com.mirjanakopanja.movieapp.model

class RepoImpl: Repository {

    override fun getMoviesFromServer(id: Int): Movies {
        val dataTransfer = MovieLoader.loadMovie(id)
        return Movies(
            title = dataTransfer?.fact?.title,
            overview = dataTransfer?.fact?.overview,
            rating = dataTransfer?.fact?.vote_average,
            date = dataTransfer?.fact?.release_date
        )
    }
    override fun getMovieFromStorage() = getMovies()
}