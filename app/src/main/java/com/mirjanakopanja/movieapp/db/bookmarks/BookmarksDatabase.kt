package com.mirjanakopanja.movieapp.db.bookmarks


import androidx.room.Room
import androidx.room.RoomDatabase
import com.mirjanakopanja.movieapp.extensions.App


@androidx.room.Database(
    entities = [
        BookmarksEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class BookmarksDatabase : RoomDatabase(){
    abstract fun BookmarksDao(): BookmarksDao

    companion object{
        const val BOOKMARK_DB_NAME = "bookmarks_database.db"
        val bookmarkDB: BookmarksDatabase by lazy {
            Room.databaseBuilder(
                App.appInstance,
                BookmarksDatabase::class.java,
                BOOKMARK_DB_NAME
            ).build()
        }
    }
}