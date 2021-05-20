package com.mirjanakopanja.movieapp.ui.description

import androidx.lifecycle.*
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.model.repository.RepoImpl
import com.mirjanakopanja.movieapp.model.repository.Repository
import retrofit2.Response

class DescriptionViewModel : ViewModel(), LifecycleObserver {

    private val repository: Repository = RepoImpl()
    val liveData: MutableLiveData<ApplicationState> = MutableLiveData()
    private val data: LiveData<ApplicationState> = liveData

    fun loadData(id: Long?): LiveData<ApplicationState> {
        getData(id)
        return data
    }

    private fun getData(id: Long?){
        liveData.value = ApplicationState.Loading

        Thread {
            val movieDetail = repository.getMoviesFromServer(id)
            liveData.postValue(ApplicationState.Success(listOf(movieDetail)))
        }.start()
    }
}