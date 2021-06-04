package com.mirjanakopanja.movieapp.model.repository




import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.data.cast.Cast
import com.mirjanakopanja.movieapp.data.tv.ShowDataTransfer
import com.mirjanakopanja.movieapp.data.video.Video

interface Repository {
    fun getMovieFromServer(id: Long?): MoviesDataTransfer
    fun getPopularMoviesListFromServer(): MoviesDataTransfer
    fun getUpcomingMoviesListFromServer(): MoviesDataTransfer
    fun getNowPlayingMoviesListFromServer(): MoviesDataTransfer
    fun getVideos(id: Long?): Video
    fun getShowTrailer(id: Long?): Video
    fun getTvShowById(id: Long?): ShowDataTransfer
    fun getPopularTvShows(): ShowDataTransfer
    fun getMovieCast(id: Long?): Cast
    fun getAdultMovies(): MoviesDataTransfer
    fun getNote(movieId: Long?) : MoviesDataTransfer
    fun saveNotes(movie: MoviesDataTransfer)
    fun insertBookmark(movie: MoviesDataTransfer)
    fun removeBookmarkById(Movie: Long?)
    fun removeBookmark(movie: MoviesDataTransfer)
    fun checkBookmarkExist(movieID: Long?): Boolean
}