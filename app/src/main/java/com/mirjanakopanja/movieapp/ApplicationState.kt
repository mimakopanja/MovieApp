package com.mirjanakopanja.movieapp

import com.mirjanakopanja.movieapp.data.MoviesDataTransfer

sealed class ApplicationState{
    data class Success(val movieData: MoviesDataTransfer): ApplicationState()
    data class Error(val error: Throwable) : ApplicationState()
    object Loading : ApplicationState()
}
