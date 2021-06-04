package com.mirjanakopanja.movieapp.data

import com.mirjanakopanja.movieapp.data.MoviesDataTransfer

data class Movies(
    var results: List<MoviesDataTransfer>? = listOf()
)