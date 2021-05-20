package com.mirjanakopanja.movieapp

import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.model.Movies

sealed class ApplicationState{
    data class Success(val movieData: List<Movies>): ApplicationState()
    data class Success1(val movieData: List<MoviesDataTransfer?>): ApplicationState()
    data class Error(val error: Throwable) : ApplicationState()
    object Loading : ApplicationState()
}
