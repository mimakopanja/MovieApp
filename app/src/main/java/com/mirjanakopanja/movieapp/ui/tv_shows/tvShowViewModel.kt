package com.mirjanakopanja.movieapp.ui.tv_shows

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.model.repository.RepoImpl
import com.mirjanakopanja.movieapp.model.repository.Repository

class tvShowViewModel (
    private val liveData: MutableLiveData<ApplicationState> = MutableLiveData()
) : ViewModel(), LifecycleObserver {

    private val repository: Repository = RepoImpl()

    fun getLiveData() = liveData

    fun getPopularShowsFromRemoteSource() {
        liveData.value = ApplicationState.Loading
        Thread{
            Thread.sleep(1000)
            liveData.postValue(
                ApplicationState.SuccessShow(
                    repository.getPopularTvShows()
             ))
        }.start()
    }
}