package com.mirjanakopanja.movieapp.ui.description

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import com.mirjanakopanja.movieapp.*
import com.mirjanakopanja.movieapp.broadcast.DescriptionService
import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.databinding.FragmentDescriprionBinding
import com.mirjanakopanja.movieapp.model.Movies


class DescriptionFragment: Fragment() {

    private var _binding: FragmentDescriprionBinding? = null
    private val binding get() = _binding!!

    private lateinit var movies: Movies

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
                            intent.getStringExtra(TITLE_EXTRA),
                            intent.getStringExtra(OVERVIEW_EXTRA),
                            intent.getStringExtra(RELEASE_DATE_EXTRA),
                            intent.getDoubleExtra(RATING_EXTRA, 0.0).toString()

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
    private fun renderData(moviesDataTransfer: MoviesDataTransfer) {
        with(binding) {

            val title = moviesDataTransfer!!.title.toString()
            val overview = moviesDataTransfer.overview.toString()
            val rating = moviesDataTransfer.vote_average.toString()
            val date = moviesDataTransfer.release_date.toString()

            descGenreTextView.text = movies.genre.toString()
            movies.image.let { it1 -> descImageView.setImageResource(it1) }


            title.let {
                descTitleTextView.text = title
                descDescTextView.text = overview
                descRatingTextView.text = rating
                releaseDateTextView.text = date
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movies = arguments?.getParcelable(BUNDLE_TAG) ?: Movies()
        val intent =  Intent(requireContext(), DescriptionService::class.java).apply {
            putExtra(ID_EXTRA, movies.id)
        }
        DescriptionService.start(requireContext(), intent)
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