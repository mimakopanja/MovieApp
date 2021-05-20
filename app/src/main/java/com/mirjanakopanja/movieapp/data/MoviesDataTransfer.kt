package com.mirjanakopanja.movieapp.data

import com.google.gson.annotations.SerializedName

data class MoviesDataTransfer(
    @SerializedName("id")
    val id: Long?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("overview")
    val overview: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("vote_average")
    val voteAverage: String?,

    @SerializedName("tagline")
    val tagline: String?,

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("poster_path")
    val posterPath: String?



)
