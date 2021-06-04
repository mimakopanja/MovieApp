package com.mirjanakopanja.movieapp.data.tv

data class TvShows(
    val page: Long? = 1,
    val results: List<ShowDataTransfer>? = listOf()
)