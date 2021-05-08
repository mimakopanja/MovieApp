package com.mirjanakopanja.movieapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.util.*

@Parcelize
data class Movie(val title: String,
                 val description: String,
                 val rating: Double,
                 val genre: Genre,
                val date: LocalDate
                 ): Parcelable
//                 val releaseDate: String
