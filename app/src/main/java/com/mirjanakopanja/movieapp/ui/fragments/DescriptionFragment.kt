package com.mirjanakopanja.movieapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.databinding.FragmentDescriprionBinding
import com.mirjanakopanja.movieapp.model.Movies

class DescriptionFragment: Fragment() {

    private var _binding: FragmentDescriprionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDescriprionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)  {
        super.onViewCreated(view, savedInstanceState)
        val movies = arguments?.getParcelable<Movies>(BUNDLE_TAG)
        movies?.let {
            with(binding) {
                val movie = it.movie
                descTitleTextView.text = movie.title
                descDescTextView.text = movie.description
                descRatingTextView.text = movie.rating.toString()
                descGenreTextView.text = movie.genre.toString()
                descImageView.setImageResource(movies.image)
                releaseDateTextView.text =
                    (getString(R.string.release_date).plus(" ").plus(movie.date))
            }
        }

    }

    companion object{
        const val BUNDLE_TAG = "movies"

        fun newInstance(bundle: Bundle): DescriptionFragment{
            val fragment = DescriptionFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}