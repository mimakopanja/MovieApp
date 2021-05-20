package com.mirjanakopanja.movieapp.popular

data class Result(
    val adult: Boolean?,
    val backdrop_path: String?,
    val id: Long?,
    val overview: String?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val vote_average: Double?
)