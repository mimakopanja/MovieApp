package com.mirjanakopanja.movieapp.model

import android.os.Parcelable
import com.mirjanakopanja.movieapp.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movies(
    val id: Long? = 582014,
    val title: String? = "Title",
    val overview: String? = "Description here.",
    val rating: Double? = 9.1,
    val genre: Genre = Genre.DRAMA,
    val release_date: String? = "16. April 2021",
    val moviePoster: Int = R.drawable.promising_young_women,
    val tagline: String? = "She created it. He sold it. And they bought it.",
    val movieImage: String? = "/dSNFyArsIZP6JcOKmXwa3rqKkOr.jpg"
): Parcelable


fun getMovies() = listOf(
        Movies(
            582014,
            "Promising Young Women","",
            7.5,
            Genre.DRAMA, "26. April 2021",
            R.drawable.promising_young_women,"",
            "/hNPwomKbMyOPXqPDqkfz34mK19p.jpg"),
        Movies(
            446791,
            "All The Money in the World",
            "",
            6.8, Genre.DRAMA,
            "05. January 2018",
            R.drawable.all_the_money, "",
            "/q6nE9Hf0ezszjI4DbCxwzQ73MMy.jpg"),
        Movies(
            306819,
            "The Danish Girl",
            "",
            7.1, Genre.DRAMA,
            "01. January 2016",
            R.drawable.danish_girl, "", "/mXZZIacI5FC8thzSC0lgQBQ2uAX.jpg"),
        Movies(
            210577,
            "Gone Girl",
            "",
            8.1, Genre.THRILLER,
            "02. October 2014",
            R.drawable.gonegirl, "",
            "/qymaJhucquUwjpb8oiqynMeXnID.jpg"),
        Movies(
            87093,
            "Big Eyes",
            "",
            7.0, Genre.BIOGRAPHY,
            "26. December 2014",
            R.drawable.big_eyes, "",
        "/203HAjJcLMl7xThcTqZx4zmEGcV.jpg"),
        Movies(
            86825,
            "Stoker",
            "",
            6.8, Genre.THRILLER,
            "01. March 2013",
            R.drawable.stoker, "",
        "/sdmykiEBpNivXMeuxaEwyc0vxyM.jpg"),
        Movies(
            601666,
            "I Care a Lot",
            "",
            6.3, Genre.THRILLER,
            "19. February 2021",
            R.drawable.i_care_a_lot, "",
        "/gKnhEsjNefpKnUdAkn7INzIFLSu.jpg"),
        Movies(
            532865,
            "The Dig",
            "",
            7.1, Genre.BIOGRAPHY,
            "29. January 2021",
            R.drawable.dig, "",
        "/dFDNb9Gk1kyLRcconpj7Mc7C7IL.jpg")
    )