package com.mirjanakopanja.movieapp

import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.data.cast.Cast
import com.mirjanakopanja.movieapp.data.cast.CastCredits

sealed class ApplicationState{
    data class Success(val data: MoviesDataTransfer): ApplicationState()
    data class SuccessCast(val cast: Cast): ApplicationState()
    data class Error(val error: Throwable) : ApplicationState()
    object Loading : ApplicationState()
}
