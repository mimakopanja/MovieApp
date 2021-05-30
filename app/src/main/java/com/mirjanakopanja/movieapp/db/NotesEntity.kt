package com.mirjanakopanja.movieapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NotesEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val movieID: Long?,
    val note: String?
)