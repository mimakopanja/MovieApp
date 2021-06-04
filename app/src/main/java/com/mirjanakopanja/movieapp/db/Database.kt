package com.mirjanakopanja.movieapp.db

import androidx.room.Room
import androidx.room.RoomDatabase
import com.mirjanakopanja.movieapp.extensions.App

@androidx.room.Database(
    entities = [
        NotesEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class Database : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object{
        private const val DB_NAME = "notes_database.db"
        val db: Database by lazy {
            Room.databaseBuilder(
                App.appInstance,
                Database::class.java,
                DB_NAME
            ).build()
        }
    }
}