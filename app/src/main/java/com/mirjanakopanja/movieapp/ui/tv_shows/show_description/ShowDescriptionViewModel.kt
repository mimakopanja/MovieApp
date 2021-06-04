package com.mirjanakopanja.movieapp.ui.tv_shows.show_description

import androidx.lifecycle.*
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.data.video.Video
import com.mirjanakopanja.movieapp.model.repository.RepoImpl
import com.mirjanakopanja.movieapp.model.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ShowDescriptionViewModel
    : ViewModel(), LifecycleObserver, CoroutineScope by MainScope() {

    private val repository: Repository = RepoImpl()

    val liveData: MutableLiveData<ApplicationState> = MutableLiveData()
    private val data: LiveData<ApplicationState> = liveData
    var trailers : MutableLiveData<Video> = MutableLiveData()

    fun loadData(id: Long?): LiveData<ApplicationState> {
        getData(id)
        return data
    }

    fun getVideo(id: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            val apiResponse = repository.getShowTrailer(id)
            trailers.postValue(apiResponse)
        }
    }

    private fun getData(id: Long?){
        liveData.value = ApplicationState.Loading
        launch(Dispatchers.IO){
            val showDetail = repository.getTvShowById(id)
            liveData.postValue(ApplicationState.SuccessShow(showDetail))
        }
    }
}