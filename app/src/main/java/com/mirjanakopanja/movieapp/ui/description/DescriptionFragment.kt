package com.mirjanakopanja.movieapp.ui.description

import android.R
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.snackbar.Snackbar
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.BuildConfig
import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.databinding.FragmentDescriprionBinding
import com.mirjanakopanja.movieapp.files.*
import com.mirjanakopanja.movieapp.model.Movies
import com.mirjanakopanja.movieapp.model.repository.ApiUtils
import com.mirjanakopanja.movieapp.model.repository.MoviesAPI
import com.mirjanakopanja.movieapp.popular.MovieResult
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DescriptionFragment: Fragment() {

    private var _binding: FragmentDescriprionBinding? = null
    private val binding get() = _binding!!

    private lateinit var movies: Movies
    private val viewModel: DescriptionViewModel by lazy {
        ViewModelProvider(this).get(DescriptionViewModel::class.java)
    }

    private fun renderData(moviesDataTransfer: MoviesDataTransfer) {
        with(binding) {

            val title = moviesDataTransfer.title.toString()
            val overview = moviesDataTransfer.overview.toString()
            val rating = moviesDataTransfer.voteAverage.toString()
            val date = moviesDataTransfer.releaseDate.toString()
            val tagline = moviesDataTransfer.tagline

            descGenreTextView.text = movies.genre.toString()
//            movies.moviePoster.let { it1 -> descImageView.setImageResource(it1) }


            title.let {
                descTitleTextView.text = title
                descDescTextView.text = overview
                descRatingTextView.text = rating
                releaseDateTextView.text = date
                descTaglineTextView.text = tagline
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movies = arguments?.getParcelable(BUNDLE_TAG) ?: Movies()

/*        val intent =  Intent(requireContext(), DescriptionService::class.java).apply {
            putExtra(ID_EXTRA, movies.id)
        }
        DescriptionService.start(requireContext(), intent)*/

        with(binding) {

            val movieId = movies.id
            descGenreTextView.text = movies.genre.toString()
//            movies.moviePoster.let { it1 -> descImageView.setImageResource(it1) }

            viewModel.liveData.observe(viewLifecycleOwner, { appState ->
                when (appState){
                    is ApplicationState.Error -> {
                        //todo
                    }
                    ApplicationState.Loading -> {
                        //todo
                    }
                    is ApplicationState.Success -> {
                        releaseDateTextView.text = appState.movieData[0].release_date
                        descTitleTextView.text = appState.movieData[0].title
                        descDescTextView.text = appState.movieData[0].overview
                        descRatingTextView.text = appState.movieData[0].rating.toString()
                        descTaglineTextView.text = appState.movieData[0].tagline
                        Picasso.get()
                            .load(BASE_URL_IMG.plus("${appState.movieData[0].movieImage}"))
                            .fit().centerCrop()
                            .into(descImageView)
                    }
                }
            })
            viewModel.loadData(movieId)
        }


    }


    private val resultReceiver: BroadcastReceiver = object :
    BroadcastReceiver(){
        @SuppressLint("ShowToast")
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)){
                DETAILS_INTENT_EMPTY_EXTRA -> Snackbar.make(view!!, DETAILS_INTENT_EMPTY_EXTRA, Snackbar.LENGTH_SHORT)
                DETAILS_DATA_EMPTY_EXTRA -> Snackbar.make(view!!, DETAILS_DATA_EMPTY_EXTRA, Snackbar.LENGTH_SHORT)
                DETAILS_RESPONSE_EMPTY_EXTRA -> Snackbar.make(view!!, DETAILS_RESPONSE_EMPTY_EXTRA, Snackbar.LENGTH_SHORT)
                DETAILS_REQUEST_ERROR_EXTRA -> Snackbar.make(view!!, DETAILS_REQUEST_ERROR_EXTRA, Snackbar.LENGTH_SHORT)
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> Snackbar.make(view!!, DETAILS_REQUEST_ERROR_MESSAGE_EXTRA, Snackbar.LENGTH_SHORT)
                DETAILS_URL_MALFORMED_EXTRA -> Snackbar.make(view!!, DETAILS_URL_MALFORMED_EXTRA, Snackbar.LENGTH_SHORT)
                DETAILS_RESPONSE_SUCCESS_EXTRA -> renderData(
                    MoviesDataTransfer(
                            intent.getLongExtra("ID", 0),
                            intent.getStringExtra(TITLE_EXTRA),
                            intent.getStringExtra(OVERVIEW_EXTRA),
                            intent.getStringExtra(RELEASE_DATE_EXTRA),
                            intent.getDoubleExtra(RATING_EXTRA, 0.0).toString(),
                            intent.getStringExtra(TAGLINE_EXTRA),
                            intent.getStringExtra("IMAGE"),
                            intent.getStringExtra("POSTER")
                    )
                )
                else -> Snackbar.make(view!!, "Error", Snackbar.LENGTH_SHORT)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDescriprionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(resultReceiver, IntentFilter(DETAILS_INTENT_FILTER))
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(resultReceiver)
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val BUNDLE_TAG = "movies"

        fun newInstance(bundle: Bundle): DescriptionFragment {
            val fragment = DescriptionFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}