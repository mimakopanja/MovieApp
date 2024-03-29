package com.mirjanakopanja.movieapp.ui.main

import androidx.lifecycle.*
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.model.repository.RepoImpl
import com.mirjanakopanja.movieapp.model.repository.Repository
import java.lang.Thread.sleep

class MainViewModel(
    private val liveData: MutableLiveData<ApplicationState> = MutableLiveData()
) : ViewModel(), LifecycleObserver {

    private val repository: Repository = RepoImpl()

    fun getLiveData() = liveData

    fun getAdultMovies() = getPopularMovieFromRemoteSource(true)
    fun getNonAdultMovies() = getPopularMovieFromRemoteSource(false)

    fun getPopularMovieFromRemoteSource(isAdult: Boolean?) {
        liveData.value = ApplicationState.Loading
        Thread{
            sleep(1000)
            liveData.postValue(ApplicationState.Success(
                if (isAdult == true) repository.getAdultMovies()
                else repository.getPopularMoviesListFromServer())
            )
        }.start()
    }

    fun getNowPlayingMovieFromRemoteSource(){
        liveData.value = ApplicationState.Loading
        Thread{
            sleep(1000)
            liveData.postValue(ApplicationState.Success(
                repository.getUpcomingMoviesListFromServer())
            )
        }.start()
    }

    fun getUpcomingMovieFromRemoteSource() {
        liveData.value = ApplicationState.Loading
        Thread{
            sleep(1000)
            liveData.postValue(ApplicationState.Success(
                repository.getUpcomingMoviesListFromServer())
            )
        }.start()
    }
}