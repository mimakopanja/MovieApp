package com.mirjanakopanja.movieapp.db.bookmarks

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mirjanakopanja.movieapp.extensions.BOOKMARK_TABLE_NAME

@Entity(tableName = BOOKMARK_TABLE_NAME)
data class BookmarksEntity(
        @PrimaryKey
        val id: Long?,
        val poster_path: String?,
        val title: String?,
        )
