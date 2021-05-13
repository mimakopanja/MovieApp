package com.mirjanakopanja.movieapp.model

import android.os.Parcelable
import com.mirjanakopanja.movieapp.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movies(
    val movie: Movie = getDefaultMovie(),
    val title: String? = "Title",
    val overview: String? = "Description here.",
    val rating: Double? = 9.1,
    val genre: Genre = Genre.DRAMA,
    val date: String? = "16. April 2021",
    val image: Int = R.drawable.promising_young_women
): Parcelable

fun getDefaultMovie() = Movie(582014)

fun getMovies() = listOf(
        Movies(
            Movie(582014),
            "Promising Young Women",
            "A young woman, traumatized by a tragic event in her past, seeks out vengeance against those who crossed her path.",
            7.5,
            Genre.DRAMA, "26. April 2021",
            R.drawable.promising_young_women),
        Movies(
            Movie(446791),
            "All The Money in the World",
            "The story of the kidnapping of 16-year-old John Paul Getty III and the desperate attempt by his devoted mother to convince his billionaire grandfather Jean Paul Getty to pay the ransom.",
            6.8, Genre.DRAMA,
            "05. January 2018",
            R.drawable.all_the_money),
        Movies(
            Movie(306819),
            "The Danish Girl",
            "A fictitious love story loosely inspired by the lives of Danish artists Lili Elbe and Gerda Wegener. Lili and Gerda's marriage and work evolve as they navigate Lili's groundbreaking journey as a transgender pioneer.",
            7.1, Genre.DRAMA,
            "01. January 2016",
            R.drawable.danish_girl),
        Movies(
            Movie(210577),
            "Gone Girl",
            "With his wife's disappearance having become the focus of an intense media circus, a man sees the spotlight turned on him when it's suspected that he may not be innocent.",
            8.1, Genre.THRILLER,
            "02. October 2014",
            R.drawable.gonegirl),
        Movies(Movie(87093),
            "Big Eyes",
            "A drama about the awakening of painter Margaret Keane, her phenomenal success in the 1950s, and the subsequent legal difficulties she had with her husband, who claimed credit for her works in the 1960s.",
            7.0, Genre.BIOGRAPHY,
            "26. December 2014",
            R.drawable.big_eyes),
        Movies(Movie(86825),
            "Stoker",
            "After India's father dies, her Uncle Charlie, whom she never knew existed, comes to live with her and her unstable mother. She comes to suspect this mysterious, charming man has ulterior motives and becomes increasingly infatuated with him.",
            6.8, Genre.THRILLER,
            "01. March 2013",
            R.drawable.stoker),
        Movies(Movie(601666),
            "I Care a Lot",
            "A crooked legal guardian who drains the savings of her elderly wards meets her match when a woman she tries to swindle turns out to be more than she first appears.",
            6.3, Genre.THRILLER,
            "19. February 2021",
            R.drawable.i_care_a_lot),
        Movies(
            Movie(532865),
            "The Dig",
            "An archaeologist embarks on the historically important excavation of Sutton Hoo in 1938.",
            7.1, Genre.BIOGRAPHY,
            "29. January 2021",
            R.drawable.dig),
        Movies(
            Movie(508442),
            "Soul",
            "After landing the gig of a lifetime, a New York jazz pianist suddenly finds himself trapped in a strange land between Earth and the afterlife.",
            8.1, Genre.ANIMATION,
            "25. December 2020",
            R.drawable.soul)
    )