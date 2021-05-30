package com.mirjanakopanja.movieapp.db

import androidx.room.*

@Dao
interface NotesDao {
    @Query("SELECT * FROM NotesEntity")
    fun getAll(): List<NotesEntity>

    @Query("SELECT * FROM NotesEntity WHERE movieID LIKE :movieID")
    fun getNote(movieID: Long?): List<NotesEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: NotesEntity)

    @Update
    fun update(entity: NotesEntity)

    @Delete
    fun delete(entity: NotesEntity)

}