package com.mirjanakopanja.movieapp.ui.description

import androidx.lifecycle.*
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.model.RepoImpl
import com.mirjanakopanja.movieapp.model.Repository

class DescriptionViewModel(
    private val repository: Repository = RepoImpl(),
    val liveData: MutableLiveData<ApplicationState> = MutableLiveData()
) : ViewModel(), LifecycleObserver {

private val data: LiveData<ApplicationState> = liveData

    fun loadData(id: Int): LiveData<ApplicationState> {
        getData(id)
        return data
    }

    private fun getData(id: Int){
        liveData.value = ApplicationState.Loading

        Thread {
            val movieDetail = repository.getMoviesFromServer(id)
            liveData.postValue(ApplicationState.Success(listOf(movieDetail)))
        }.start()
    }
}