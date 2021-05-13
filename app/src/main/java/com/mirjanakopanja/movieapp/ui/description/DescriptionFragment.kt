package com.mirjanakopanja.movieapp.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.databinding.FragmentDescriprionBinding
import com.mirjanakopanja.movieapp.model.Movies
import kotlinx.android.synthetic.main.main_fragment.*

@Suppress("NAME_SHADOWING")
class DescriptionFragment: Fragment() {

    private lateinit var binding: FragmentDescriprionBinding

    private val viewModel: DescriptionViewModel by lazy {
        ViewModelProvider(this).get(DescriptionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDescriprionBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)  {
        super.onViewCreated(view, savedInstanceState)
        val movies = arguments?.getParcelable<Movies>(BUNDLE_TAG)

        movies?.let {
            with(binding) {
                val movieId = it.movie.id

                descGenreTextView.text = it.genre.toString()
                movies.image?.let { it1 -> descImageView.setImageResource(it1) }

                viewModel.liveData.observe(this@DescriptionFragment, { appState ->
                    when (appState){
                        is ApplicationState.Error -> {
                            //todo
                        }
                        ApplicationState.Loading -> {
                            //todo
                        }
                        is ApplicationState.Success -> {
                            releaseDateTextView.text = appState.movie[0].date
                            descTitleTextView.text = appState.movie[0].title
                            descDescTextView.text = appState.movie[0].overview
                            descRatingTextView.text = appState.movie[0].rating.toString()
                        }
                    }
                })
                viewModel.loadData(movieId)
            }
        }
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