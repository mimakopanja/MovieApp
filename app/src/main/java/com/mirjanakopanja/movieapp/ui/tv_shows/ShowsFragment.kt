package com.mirjanakopanja.movieapp.ui.tv_shows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.data.tv.ShowDataTransfer
import com.mirjanakopanja.movieapp.data.tv.TvShows
import com.mirjanakopanja.movieapp.databinding.FragmentShowsBinding
import com.mirjanakopanja.movieapp.extensions.showToast
import com.mirjanakopanja.movieapp.model.api.MoviesAPI
import com.mirjanakopanja.movieapp.ui.description.DescriptionFragment
import com.mirjanakopanja.movieapp.ui.tv_shows.show_description.ShowsDescriptionFragment
import kotlinx.android.synthetic.main.fragment_shows.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsFragment: Fragment() {
    private lateinit var binding: FragmentShowsBinding
    private var showId : Long? = 0

    private val viewModel: tvShowViewModel by lazy {
        ViewModelProvider(this).get(tvShowViewModel::class.java)
    }

    private val adapterPopularShows = TvShowsAdapter(object : OnItemClick {
        override fun onItemClick(shows: ShowDataTransfer?) {
            onCLick(shows)
        }
    })

    private fun onCLick(shows: ShowDataTransfer?) {
        val manager = activity?.supportFragmentManager
        if (manager != null) {
            val bundle = Bundle()
            bundle.putParcelable(ShowsDescriptionFragment.BUNDLE_SHOW_TAG, shows)
            manager.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.container, ShowsDescriptionFragment.newInstance(bundle))
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    companion object {
        fun newInstance() = ShowsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): ConstraintLayout? {
        binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerShows.adapter = adapterPopularShows

        viewModel.getLiveData().observe(viewLifecycleOwner, { processData(it)})
        viewModel.getPopularShowsFromRemoteSource()
    }

    private fun processData(applicationState: ApplicationState?) {
        when(applicationState){
            is ApplicationState.Error -> {
                shows_loading_layout.visibility = View.GONE
                shows_liner_layout.visibility = View.GONE
                showToast("Error Loading Tv Shows!")
            }
            ApplicationState.Loading -> {
                shows_loading_layout.visibility = View.VISIBLE
            }
            is ApplicationState.SuccessShow -> {
                shows_loading_layout.visibility = View.GONE
                loadPopularShows()
            }
        }
    }

    private fun loadPopularShows() {
        val moviesApi = MoviesAPI.create().getPopularTvShows()
        moviesApi.enqueue(object : Callback<TvShows> {
            override fun onResponse(call: Call<TvShows>, response: Response<TvShows>) {
                if (response.body() != null) {
                    adapterPopularShows.setMovieListItems(response.body()!!)
                    showId = response.body()?.results?.first()?.id
                }
            }
            override fun onFailure(call: Call<TvShows>, t: Throwable) {
            }

        })
    }

    interface OnItemClick{
        fun onItemClick(shows: ShowDataTransfer?)
    }
}