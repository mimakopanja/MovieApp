package com.mirjanakopanja.movieapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class MoviesDataTransfer(
    @SerializedName("id")
    val id: Long? = 210577,

    @SerializedName("adult")
    val adult: Boolean? = true,

    @SerializedName("title")
    val title: String? = "Title",

    @SerializedName("overview")
    val overview: String? = "Overview",


    @SerializedName("genres")
    val genres: @RawValue List<Genre>? = listOf(),

    @SerializedName("release_date")
    val release_date: String? = "01.01.2021",

    @SerializedName("vote_average")
    val vote_average: String? = "9.9",

    @SerializedName("tagline")
    val tagline: String? = "Tagline",

    @SerializedName("backdrop_path")
    val backdrop_path: String? = "backdrop",

    @SerializedName("poster_path")
    val poster_path: String? = "poster",

    var note: String? = "",

    var bookmark: Boolean? = false
) : Parcelable