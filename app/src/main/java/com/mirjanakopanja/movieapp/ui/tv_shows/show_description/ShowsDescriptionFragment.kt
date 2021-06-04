package com.mirjanakopanja.movieapp.ui.tv_shows.show_description

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.data.Genre
import com.mirjanakopanja.movieapp.data.tv.ShowDataTransfer
import com.mirjanakopanja.movieapp.databinding.ShowDescriptionBinding
import com.mirjanakopanja.movieapp.extensions.*
import com.mirjanakopanja.movieapp.ui.description.VideoPlayerDialog
import com.squareup.picasso.Picasso
import kotlinx.android.parcel.RawValue

class ShowsDescriptionFragment: Fragment() {


    private var _binding: ShowDescriptionBinding? = null
    private val binding get() = _binding!!

    private lateinit var shows: ShowDataTransfer

    private val seasonsAdapter = SeasonsAdapter()

    private val viewModel: ShowDescriptionViewModel by lazy {
        ViewModelProvider(this).get(ShowDescriptionViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        shows = arguments?.getParcelable(BUNDLE_SHOW_TAG) ?: ShowDataTransfer()

        binding.recyclerViewSeasons.adapter = seasonsAdapter
        loadData()
        loadVideo()
    }

    private fun loadVideo() {
        viewModel.getVideo(shows.id)
        binding.playShowsBtn.setOnClickListener {
            if (viewModel.trailers.value != null && viewModel.trailers.value?.size != 0) {
                val videoDialog = viewModel.trailers.value?.key?.let { it1 -> VideoPlayerDialog(it1) }
                videoDialog?.show(childFragmentManager, "Video Dialog")
            }
            else {
                showToast("Video not found!")
            }
        }
    }

    private fun loadData() {
        with(binding) {
            val showId = shows.id
            viewModel.liveData.observe(viewLifecycleOwner, { appState ->
                when (appState) {
                    is ApplicationState.Error -> {
                        showToast("Error")
                    }
                    ApplicationState.Loading -> {
                        //todo
                    }
                    is ApplicationState.SuccessShow -> {
                        val genreList: @RawValue List<Genre>? = appState.data.genres
                        val sb = StringBuffer()
                        sb.append(genreList?.first()?.name)
                        if (genreList != null) {
                            for (i in 1 until genreList.size) {
                                sb.append(", ")
                                sb.append(genreList[i].name)
                            }
                        }
                        showGenreTextView.text = sb.toString()
                        showReleaseDateTextView.text = appState.data.first_air_date
                        showTitleTextView.text = appState.data.name
                        showDescTextView.text = appState.data.overview
                        showRatingTextView.text = appState.data.vote_average.toString().plus(" / 10")
                        showTaglineTextView.text = appState.data.tagline

                        Picasso.get()
                            .load(BASE_URL_IMG.plus("${appState.data.backdrop_path}"))
                            .placeholder(R.drawable.ic_movie_yellow)
                            .fit().centerCrop()
                            .into(showImageView)

                        appState.data.seasons?.let { seasonsAdapter.updateSeasons(it) }
                    }
                }
            })
            viewModel.loadData(showId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = ShowDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val BUNDLE_SHOW_TAG = "shows"

        fun newInstance(bundle: Bundle): ShowsDescriptionFragment {
            val fragment = ShowsDescriptionFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    interface OnItemClick {
        fun onItemClick(seasonId: Int?)
    }
}