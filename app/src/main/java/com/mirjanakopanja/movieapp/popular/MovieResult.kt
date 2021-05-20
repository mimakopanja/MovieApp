package com.mirjanakopanja.movieapp.popular

data class MovieResult(
    val page: Int?,
    val results: List<Result>?,
    val total_pages: Int?,
    val total_results: Int?
)