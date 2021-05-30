package com.mirjanakopanja.movieapp.model.repository



import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.data.cast.Cast

interface Repository {
    fun getMovieFromServer(id: Long?): MoviesDataTransfer
    fun getPopularMoviesListFromServer(): MoviesDataTransfer
    fun getUpcomingMoviesListFromServer(): MoviesDataTransfer
    fun getNowPlayingMoviesListFromServer(): MoviesDataTransfer
    fun getMovieCast(id: Long?): Cast
    fun getAdultMovies(): MoviesDataTransfer
    fun getNote(movieId: Long?) : MoviesDataTransfer
    fun saveNotes(movie: MoviesDataTransfer)
    fun insertBookmark(movie: MoviesDataTransfer)
    fun removeBookmarkById(Movie: Long?)
    fun removeBookmark(movie: MoviesDataTransfer)
    fun checkBookmarkExist(movieID: Long?): Boolean
}