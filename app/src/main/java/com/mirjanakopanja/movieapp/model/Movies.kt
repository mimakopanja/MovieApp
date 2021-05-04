package com.mirjanakopanja.movieapp.model

import com.mirjanakopanja.movieapp.R


data class Movies(val title: String = "Big Eyes",
                  val year: Int = 2014,
                  val image: Int = R.drawable.big_eyes
)

fun getMovies(): List<Movies>{
    return listOf(
        Movies("Promising Young Women", 2020, R.drawable.promising_young_women),
        Movies("All the Money in the World", 2017, R.drawable.all_the_money),
        Movies("The Danish Girl", 2015, R.drawable.danish_girl),
        Movies("Gone Girl", 2014, R.drawable.gonegirl),
        Movies("Big Eyes", 2014, R.drawable.big_eyes),
        Movies("Stoker", 2013, R.drawable.stoker),
        Movies("I care a Lot", 2020, R.drawable.i_care_a_lot),
        Movies("The Dig", 2021, R.drawable.dig)
    )
}