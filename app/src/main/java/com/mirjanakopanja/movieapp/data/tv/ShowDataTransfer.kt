package com.mirjanakopanja.movieapp.data.tv

import android.os.Parcelable
import com.mirjanakopanja.movieapp.data.Genre
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ShowDataTransfer(
    val backdrop_path: String? = "",
    val episode_run_time: List<Int>? = listOf(),
    val genres: @RawValue List<Genre>? = listOf(),
    val id: Long? = 1,
    val name: String? = "",
    val number_of_episodes: Int? = 1,
    val number_of_seasons: Int? = 1,
    val overview: String? = "",
    val poster_path: String? = "",
    val seasons: @RawValue List<Season>? = listOf(),
    val tagline: String? = "",
    val vote_average: Double? = 1.1,
    val first_air_date: String? = ""
): Parcelable