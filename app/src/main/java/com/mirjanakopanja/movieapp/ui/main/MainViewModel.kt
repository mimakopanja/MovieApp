package com.mirjanakopanja.movieapp.ui.main

import androidx.lifecycle.*
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.model.repository.RepoImpl
import com.mirjanakopanja.movieapp.model.repository.Repository

class MainViewModel(
    private val liveData: MutableLiveData<ApplicationState> = MutableLiveData()
) : ViewModel(), LifecycleObserver {

    private val repository: Repository = RepoImpl()

    fun getLiveData() = liveData
    fun getMovie() = getMovieFromLocalSource()

    fun getMovieFromLocalSource() {
        liveData.value = ApplicationState.Loading

        Thread{
            liveData.postValue(ApplicationState.Success(repository.getMovieFromStorage()))
        }.start()
    }
}