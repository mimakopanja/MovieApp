package com.mirjanakopanja.movieapp.model.repository


import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.data.cast.Cast
import com.mirjanakopanja.movieapp.data.tv.Season
import com.mirjanakopanja.movieapp.data.tv.ShowDataTransfer
import com.mirjanakopanja.movieapp.data.video.Video
import com.mirjanakopanja.movieapp.db.Database
import com.mirjanakopanja.movieapp.db.NotesEntity
import com.mirjanakopanja.movieapp.db.bookmarks.BookmarksDatabase
import com.mirjanakopanja.movieapp.db.bookmarks.BookmarksEntity
import com.mirjanakopanja.movieapp.model.api.MoviesApiRepo

class RepoImpl: Repository {

    override fun getMovieFromServer(id: Long?): MoviesDataTransfer {
        val dataTransfer = MoviesApiRepo.api.getMovieById(id).execute().body()
        return MoviesDataTransfer(
            id = dataTransfer?.id,
            title = dataTransfer?.title,
            overview = dataTransfer?.overview,
            vote_average = dataTransfer?.vote_average,
            release_date = dataTransfer?.release_date,
            tagline = dataTransfer?.tagline,
            backdrop_path = dataTransfer?.backdrop_path,
            poster_path = dataTransfer?.poster_path,
            genres = dataTransfer?.genres,
            note = dataTransfer?.note
        )
    }

    override fun getUpcomingMoviesListFromServer(): MoviesDataTransfer {
        val data = MoviesApiRepo.api.getUpcomingMovies().execute().body()
        return MoviesDataTransfer(
            id = data?.results?.first()?.id,
            title = data?.results?.first()?.title,
            vote_average = data?.results?.first()?.vote_average,
            adult = data?.results?.first()?.adult
        )
    }

    override fun getNowPlayingMoviesListFromServer(): MoviesDataTransfer {
        val data = MoviesApiRepo.api.getNowPlayingMovies().execute().body()
        return MoviesDataTransfer(
            id = data?.results?.first()?.id,
            title = data?.results?.first()?.title,
            vote_average = data?.results?.first()?.vote_average,
            adult = data?.results?.first()?.adult
        )
    }

    override fun getPopularMoviesListFromServer(): MoviesDataTransfer {
        val data = MoviesApiRepo.api.getPopularMovies().execute().body()
        return MoviesDataTransfer(
            id = data?.results?.first()?.id,
            title = data?.results?.first()?.title,
            vote_average = data?.results?.first()?.vote_average,
            adult = data?.results?.first()?.adult
        )
    }

    override fun getVideos(id: Long?): Video {
        val videoData = MoviesApiRepo.api.getVideos(id).execute().body()
        return Video(
            id = videoData?.results?.first()?.id,
            key = videoData?.results?.first()?.key,
            name = videoData?.results?.first()?.name,
            site = videoData?.results?.first()?.site,
            size = videoData?.results?.first()?.size,
            type = videoData?.results?.first()?.type
        )
    }

    override fun getShowTrailer(id: Long?): Video {
        val showTrailerData = MoviesApiRepo.api.getShowTrailer(id).execute().body()
        return Video(
            id = showTrailerData?.results?.first()?.id,
            key = showTrailerData?.results?.first()?.key,
            name = showTrailerData?.results?.first()?.name,
            size = showTrailerData?.results?.first()?.size,
            site = showTrailerData?.results?.first()?.site,
            type = showTrailerData?.results?.first()?.type
        )
    }

    override fun getAdultMovies(): MoviesDataTransfer {
        val data = MoviesApiRepo.api.getPopularMovies().execute().body()
        if (data?.results?.first()?.adult == true){
            return MoviesDataTransfer(
                id = data?.results?.first()?.id,
                title = data?.results?.first()?.title,
                vote_average = data?.results?.first()?.vote_average,
                adult = data?.results?.first()?.adult
            )
        }
        return getPopularMoviesListFromServer()
    }

    override fun getMovieCast(id: Long?): Cast {
        val castData = MoviesApiRepo.api.getMovieCredits(id).execute().body()
        return Cast(
            id = castData?.cast?.first()?.id,
            name = castData?.cast?.first()?.name,
            original_name = castData?.cast?.first()?.original_name,
            popularity = castData?.cast?.first()?.popularity,
            profile_path = castData?.cast?.first()?.profile_path
        )
    }

    override fun getTvShowById(id: Long?): ShowDataTransfer {
        val tvShowData = MoviesApiRepo.api.getTvShowById(id).execute().body()
        return ShowDataTransfer(
            backdrop_path = tvShowData?.backdrop_path,
            episode_run_time = tvShowData?.episode_run_time,
            genres = tvShowData?.genres,
            id = tvShowData?.id,
            name = tvShowData?.name,
            number_of_episodes = tvShowData?.number_of_episodes,
            number_of_seasons = tvShowData?.number_of_seasons,
            overview = tvShowData?.overview,
            poster_path = tvShowData?.poster_path,
            seasons = tvShowData?.seasons,
            tagline = tvShowData?.tagline,
            vote_average = tvShowData?.vote_average,
            first_air_date = tvShowData?.first_air_date
        )
    }

    override fun getPopularTvShows(): ShowDataTransfer {
        val tvShowDataPopular = MoviesApiRepo.api.getPopularTvShows().execute().body()
        return ShowDataTransfer(
            id = tvShowDataPopular?.results?.first()?.id,
            name = tvShowDataPopular?.results?.first()?.name,
            vote_average = tvShowDataPopular?.results?.first()?.vote_average
        )
    }

    override fun getNote(movieId: Long?): MoviesDataTransfer {
        return convertEntityToMovie(Database.db.notesDao().getNote(movieId))
    }

    override fun saveNotes(movie: MoviesDataTransfer) {
        Database.db.notesDao().insert(convertMoviesToEntity(movie))
    }

     private fun convertMoviesToEntity(movie: MoviesDataTransfer): NotesEntity =
        NotesEntity(0,
            movie.id ?: 0,
            movie.note ?: ""
        )

    private fun convertEntityToMovie(entityList: List<NotesEntity>): MoviesDataTransfer {
        if (entityList.isEmpty()){
            return MoviesDataTransfer(note = "No note")
        }
        return MoviesDataTransfer(id = entityList.last().movieID, note = entityList.last().note)
    }

    private fun convertMoviesToBookmark(movie: MoviesDataTransfer): BookmarksEntity =
        BookmarksEntity(movie.id ?: 0,
            movie.poster_path ?: "",
            movie.title ?: ""
        )

    private fun convertBookmarkToMovie(entity: BookmarksEntity): MoviesDataTransfer {
        return MoviesDataTransfer(id = entity.id, poster_path = entity.poster_path, title = entity.title)
    }

    override fun insertBookmark(movie: MoviesDataTransfer) {
        BookmarksDatabase.bookmarkDB.BookmarksDao().insertBookmark(convertMoviesToBookmark(movie))
    }

    override fun removeBookmarkById(movieID: Long?) {
        BookmarksDatabase.bookmarkDB.BookmarksDao().deleteByMovieId(movieID)
    }

    override fun removeBookmark(movie: MoviesDataTransfer) {
        BookmarksDatabase.bookmarkDB.BookmarksDao().removeBookmark(convertMoviesToBookmark(movie))
    }

    override fun checkBookmarkExist(movieID: Long?) : Boolean {
       if(BookmarksDatabase.bookmarkDB.BookmarksDao().bookmarkExist(movieID)){
           return true
       }
        return false
    }
}