package com.mirjanakopanja.movieapp.model.repository


import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.model.Movies
import com.mirjanakopanja.movieapp.model.getMovies
import com.mirjanakopanja.movieapp.popular.Result
import retrofit2.Callback

class RepoImpl: Repository {

    override fun getMoviesFromServerAsync(id: Long?, callback: Callback<MoviesDataTransfer>) {
        MoviesApiRepo.api.getMovieById(id).enqueue(callback)
    }

    override fun getMoviesFromServer(id: Long?): Movies {
        val dataTransfer = MoviesApiRepo.api.getMovieById(id).execute().body()
        return Movies(
            id= dataTransfer?.id,
            title = dataTransfer?.title,
            overview = dataTransfer?.overview,
            rating = dataTransfer?.voteAverage?.toDouble(),
            release_date = dataTransfer?.releaseDate,
            tagline = dataTransfer?.tagline,
            movieImage = dataTransfer?.backdropPath
        )
    }



    override fun getMovieFromStorage() = getMovies()

    override fun getMovieListFromSite(): List<MoviesDataTransfer?> {
        val dataT = MoviesApiRepo.api.getPopularList().execute().body()
        return listOf(
            MoviesDataTransfer(
            id = dataT?.id,
            overview = dataT?.overview,
            releaseDate = dataT?.releaseDate,
            title = dataT?.title,
                voteAverage = dataT?.voteAverage,
                tagline = dataT?.tagline,
                backdropPath = dataT?.backdropPath,
                posterPath = dataT?.posterPath
        ))
    }
}