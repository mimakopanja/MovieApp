package com.mirjanakopanja.movieapp.ui.main

import androidx.lifecycle.*
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.model.RepoImpl
import com.mirjanakopanja.movieapp.model.Repository

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<ApplicationState> = MutableLiveData()
) : ViewModel(), LifecycleObserver {

    private val repository: Repository = RepoImpl()
    private val lifeCycleLiveData = MutableLiveData<String>()

    fun getLiveData() = liveDataToObserve
    fun getMovie() = getMovieFromLocalSource()
    fun getData(): LiveData<ApplicationState>{
        getMovieFromLocalSource()
        return liveDataToObserve
    }
    fun getLifeCycleData() = lifeCycleLiveData

    fun getMovieFromLocalSource() {
        liveDataToObserve.value = ApplicationState.Loading
        Thread{
            liveDataToObserve.postValue(ApplicationState.Success(repository.getMovieFromStorage()))
        }.start()
    }
}