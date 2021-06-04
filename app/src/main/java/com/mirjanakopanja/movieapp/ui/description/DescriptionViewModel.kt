package com.mirjanakopanja.movieapp.ui.description


import androidx.lifecycle.*
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.data.video.Video
import com.mirjanakopanja.movieapp.data.video.VideoResponse
import com.mirjanakopanja.movieapp.model.repository.RepoImpl
import com.mirjanakopanja.movieapp.model.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalStateException

class DescriptionViewModel(
) : ViewModel(), LifecycleObserver, CoroutineScope by MainScope() {

    private val repository: Repository = RepoImpl()
    val liveData: MutableLiveData<ApplicationState> = MutableLiveData()
    private val data: LiveData<ApplicationState> = liveData
    var videos : MutableLiveData<Video> = MutableLiveData()


    fun loadData(id: Long?): LiveData<ApplicationState> {
        getData(id)
        loadCast(id)
        return data
    }

    fun getVideo(id: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            val apiResponse = repository.getVideos(id)
            videos.postValue(apiResponse)
        }
    }

    private fun getData(id: Long?){
        liveData.value = ApplicationState.Loading
        launch(Dispatchers.IO){
            val movieDetail = repository.getMovieFromServer(id)
            liveData.postValue(ApplicationState.Success(movieDetail))
        }
    }

    fun loadCast(id: Long?) {
        liveData.value = ApplicationState.Loading
        Thread{
            Thread.sleep(1000)
            liveData.postValue(ApplicationState.SuccessCast(
                repository.getMovieCast(id)
            ))
        }.start()
    }

    fun loadDataAsync(id: Long?){
        val callback = object : Callback<MoviesDataTransfer>{
            override fun onResponse(
                call: Call<MoviesDataTransfer>,
                response: Response<MoviesDataTransfer>
            ) {
                if (response.isSuccessful){
                    val model = MoviesDataTransfer(
                        id = response.body()?.id,
                        adult = response.body()?.adult,
                        title = response.body()?.title,
                        overview = response.body()?.overview,
                        genres = response.body()?.genres,
                        release_date = response.body()?.release_date,
                        vote_average = response.body()?.vote_average,
                        tagline = response.body()?.tagline,
                        backdrop_path = response.body()?.backdrop_path,
                        poster_path = response.body()?.poster_path,
                        note = response.body()?.note
                    )
                    launch(Dispatchers.IO){ repository.saveNotes(model) }
                    liveData.postValue(ApplicationState.Success(model))
                } else {
                    liveData.postValue(ApplicationState.Error(IllegalStateException()))
                }
            }
            override fun onFailure(call: Call<MoviesDataTransfer>, t: Throwable) {
                t.printStackTrace()
            }

        }
    }
}