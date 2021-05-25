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
import com.mirjanakopanja.movieapp.databinding.MainFragmentBinding
import com.mirjanakopanja.movieapp.model.api.MoviesAPI
import com.mirjanakopanja.movieapp.data.Movies
import com.mirjanakopanja.movieapp.ui.description.DescriptionFragment
import com.mirjanakopanja.movieapp.ui.MoviesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : Fragment() {
    private  var _binding: MainFragmentBinding? = null
    private val binding get() = _binding
    private var movieID : Long? = 0
    private var isAdult: Boolean? = true

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val adapter = MoviesAdapter(object : OnItemClick {
        override fun onItemClick(movies: MoviesDataTransfer?) {
            val manager = activity?.supportFragmentManager
            if (manager != null){
                val bundle = Bundle()
                bundle.putParcelable(DescriptionFragment.BUNDLE_TAG, movies)
                manager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.container, DescriptionFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })

    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): ConstraintLayout? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.recyclerView?.adapter = adapter


        viewModel.getLiveData().observe(viewLifecycleOwner, { processData(it)})
        viewModel.getMovieFromRemoteSource(isAdult)

/*        val moviesApi = MoviesAPI.create().getPopularMovies()
        moviesApi.enqueue(object : Callback<MovieClass> {
            override fun onResponse(call: Call<MovieClass>, response: Response<MovieClass>) {
                if (response.body() != null){
                    adapter.setMovieListItems(response.body()!!)
                }
            }

            override fun onFailure(call: Call<MovieClass>, t: Throwable) {
            }

        })*/


    }

    private fun processData(applicationState: ApplicationState?) {
        when(applicationState){
            is ApplicationState.Error -> {
                binding?.loadingFrameLayout?.visibility  = View.GONE
            }
            ApplicationState.Loading -> {
                binding?.loadingFrameLayout?.visibility  = View.VISIBLE
            }
            is ApplicationState.Success -> {
                binding?.loadingFrameLayout?.visibility = View.GONE
                val moviesApi = MoviesAPI.create().getPopularMovies()
                moviesApi.enqueue(object : Callback<Movies> {
                    override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                        if (response.body() != null){
                            adapter.setMovieListItems(response.body()!!)
                            movieID = response.body()?.results?.get(0)?.id
                            isAdult = response.body()?.results?.get(0)?.adult
                        }
                    }

                    override fun onFailure(call: Call<Movies>, t: Throwable) {
                    }

                })
            }
        }
    }


    interface OnItemClick{
        fun onItemClick(movies: MoviesDataTransfer?)
    }
}