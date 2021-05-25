package com.mirjanakopanja.movieapp.model.repository



import com.mirjanakopanja.movieapp.data.MoviesDataTransfer

interface Repository {
    fun getMovieFromServer(id: Long?): MoviesDataTransfer
    fun getMoviesListFromServer(): MoviesDataTransfer
    fun getAdultMovies(): MoviesDataTransfer
    fun getNote(movieId: Long?) : MoviesDataTransfer
    fun saveNotes(movie: MoviesDataTransfer)
    fun insertBookmark(movie: MoviesDataTransfer)
    fun removeBookmarkById(Movie: Long?)
    fun removeBookmark(movie: MoviesDataTransfer)
    fun checkBookmarkExist(movieID: Long?): Boolean
}