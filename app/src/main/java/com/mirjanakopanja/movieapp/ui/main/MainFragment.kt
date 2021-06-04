package com.mirjanakopanja.movieapp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.model.api.MoviesAPI
import com.mirjanakopanja.movieapp.data.Movies
import com.mirjanakopanja.movieapp.databinding.FragmentMainBinding
import com.mirjanakopanja.movieapp.extensions.showToast
import com.mirjanakopanja.movieapp.ui.description.DescriptionFragment
import com.mirjanakopanja.movieapp.ui.main.popular.NowPlayingAdapter
import com.mirjanakopanja.movieapp.ui.main.popular.PopularMoviesAdapter
import com.mirjanakopanja.movieapp.ui.main.popular.UpcomingMovieAdapter
import kotlinx.android.synthetic.main.fragment_descriprion.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_movie.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private var movieID : Long? = 0
    private var isAdult: Boolean? = true

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val adapterUpcoming = UpcomingMovieAdapter(object : OnItemClick {
        override fun onItemClick(movies: MoviesDataTransfer?) {
            onCLick(movies)
        }
    })

    private val adapterPopular = PopularMoviesAdapter(object : OnItemClick {
        override fun onItemClick(movies: MoviesDataTransfer?) {
            onCLick(movies)
        }
    })

    private val adapterNowPaying = NowPlayingAdapter(object : OnItemClick {
        override fun onItemClick(movies: MoviesDataTransfer?) {
            onCLick(movies)
        }
    })

    private fun onCLick(movies: MoviesDataTransfer?) {
        val manager = activity?.supportFragmentManager
        if (manager != null) {
            val bundle = Bundle()
            bundle.putParcelable(DescriptionFragment.BUNDLE_TAG, movies)
            manager.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.container, DescriptionFragment.newInstance(bundle))
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    override fun onDestroy() {
        adapterPopular.removeListener()
        super.onDestroy()
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): ConstraintLayout? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerPopular.adapter = adapterPopular
        binding.recyclerUpcoming.adapter = adapterUpcoming
        binding.recyclerNowPlaying.adapter = adapterNowPaying

        viewModel.getLiveData().observe(viewLifecycleOwner, { processData(it)})
        viewModel.getPopularMovieFromRemoteSource(isAdult)
        viewModel.getUpcomingMovieFromRemoteSource()
        viewModel.getNowPlayingMovieFromRemoteSource()
    }

    private fun processData(applicationState: ApplicationState?) {
        when(applicationState){
            is ApplicationState.Error -> {
                loadingFrameLayout.visibility = View.GONE
                scrollView.visibility = View.GONE
                showToast("Error Loading Movies!")
            }
            ApplicationState.Loading -> {
                loadingFrameLayout.visibility = View.VISIBLE
                scrollView.visibility = View.GONE

            }
            is ApplicationState.Success -> {
                loadingFrameLayout.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
                loadPopular()
                loadUpcoming()
                loadNowPlaying()
            }
        }
    }

    private fun loadNowPlaying() {
        val moviesApi = MoviesAPI.create().getNowPlayingMovies()
        moviesApi.enqueue(object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                if (response.body() != null) {
                    adapterNowPaying.setMovieListItems(response.body()!!)
                    movieID = response.body()?.results?.get(0)?.id
                    isAdult = response.body()?.results?.get(0)?.adult
                }
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
            }

        })
    }

    private fun loadUpcoming() {
        val moviesApi = MoviesAPI.create().getUpcomingMovies()
        moviesApi.enqueue(object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                if (response.body() != null) {
                    adapterUpcoming.setMovieListItems(response.body()!!)
                    movieID = response.body()?.results?.get(0)?.id
                    isAdult = response.body()?.results?.get(0)?.adult
                }
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
            }

        })
    }

    private fun loadPopular() {
        val moviesApi = MoviesAPI.create().getPopularMovies()
        moviesApi.enqueue(object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                if (response.body() != null) {
                    adapterPopular.setMovieListItems(response.body()!!)
                    movieID = response.body()?.results?.get(0)?.id
                    isAdult = response.body()?.results?.get(0)?.adult
                }
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
            }

        })
    }

    interface OnItemClick{
        fun onItemClick(movies: MoviesDataTransfer?)
    }
}