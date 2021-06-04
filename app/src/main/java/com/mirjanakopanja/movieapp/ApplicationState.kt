package com.mirjanakopanja.movieapp

import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.data.cast.Cast
import com.mirjanakopanja.movieapp.data.cast.CastCredits
import com.mirjanakopanja.movieapp.data.tv.ShowDataTransfer

sealed class ApplicationState{
    data class Success(val data: MoviesDataTransfer): ApplicationState()
    data class SuccessShow(val data: ShowDataTransfer): ApplicationState()
    data class SuccessCast(val cast: Cast): ApplicationState()
    data class Error(val error: Throwable) : ApplicationState()
    object Loading : ApplicationState()
}
