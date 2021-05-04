package com.mirjanakopanja.movieapp

import com.mirjanakopanja.movieapp.model.Movies

sealed class ApplicationState{
    data class Success(val movie: List<Movies>): ApplicationState()
    data class Error(val error: Throwable) : ApplicationState()
    object Loading : ApplicationState()
}
