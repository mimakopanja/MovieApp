package com.mirjanakopanja.movieapp.model.repository


import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.data.cast.Cast
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
            id = data?.results?.get(0)?.id,
            title = data?.results?.get(0)?.title,
            vote_average = data?.results?.get(0)?.vote_average,
            adult = data?.results?.get(0)?.adult
        )
    }

    override fun getNowPlayingMoviesListFromServer(): MoviesDataTransfer {
        val data = MoviesApiRepo.api.getNowPlayingMovies().execute().body()
        return MoviesDataTransfer(
            id = data?.results?.get(0)?.id,
            title = data?.results?.get(0)?.title,
            vote_average = data?.results?.get(0)?.vote_average,
            adult = data?.results?.get(0)?.adult
        )
    }

    override fun getPopularMoviesListFromServer(): MoviesDataTransfer {
        val data = MoviesApiRepo.api.getPopularMovies().execute().body()
        return MoviesDataTransfer(
            id = data?.results?.get(0)?.id,
            title = data?.results?.get(0)?.title,
            vote_average = data?.results?.get(0)?.vote_average,
            adult = data?.results?.get(0)?.adult
        )
    }

    override fun getAdultMovies(): MoviesDataTransfer {
        val data = MoviesApiRepo.api.getPopularMovies().execute().body()
        if (data?.results?.get(0)?.adult == true){
            return MoviesDataTransfer(
                id = data?.results?.get(0)?.id,
                title = data?.results?.get(0)?.title,
                vote_average = data?.results?.get(0)?.vote_average,
                adult = data?.results?.get(0)?.adult
            )
        }
        return getPopularMoviesListFromServer()
    }

    override fun getMovieCast(id: Long?): Cast {
        val castData = MoviesApiRepo.api.getMovieCredits(id).execute().body()
        return Cast(
            id = castData?.cast?.get(0)?.id,
            name = castData?.cast?.get(0)?.name,
            original_name = castData?.cast?.get(0)?.original_name,
            popularity = castData?.cast?.get(0)?.popularity,
            profile_path = castData?.cast?.get(0)?.profile_path
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