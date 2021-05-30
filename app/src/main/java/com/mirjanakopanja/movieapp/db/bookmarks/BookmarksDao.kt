package com.mirjanakopanja.movieapp.db.bookmarks

import androidx.room.*
import com.mirjanakopanja.movieapp.extensions.BOOKMARK_TABLE_NAME

@Dao
interface BookmarksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertBookmark(Movie: BookmarksEntity)

    @Delete
     fun removeBookmark(movie: BookmarksEntity)

    @Query("DELETE FROM $BOOKMARK_TABLE_NAME WHERE id LIKE :movieID" )
     fun deleteByMovieId(movieID: Long?)

    @Query("SELECT * FROM $BOOKMARK_TABLE_NAME")
     fun getAllBookmarks() : List<BookmarksEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM $BOOKMARK_TABLE_NAME WHERE id LIKE :movieID)")
     fun bookmarkExist(movieID: Long?): Boolean
}