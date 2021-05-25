package com.mirjanakopanja.movieapp.ui.description


import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.data.Genre
import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.databinding.FragmentDescriprionBinding
import com.mirjanakopanja.movieapp.db.Database
import com.mirjanakopanja.movieapp.db.NotesEntity
import com.mirjanakopanja.movieapp.extensions.*
import com.mirjanakopanja.movieapp.model.repository.RepoImpl
import com.mirjanakopanja.movieapp.model.repository.Repository
import com.squareup.picasso.Picasso
import kotlinx.android.parcel.RawValue
import kotlinx.android.synthetic.main.fragment_descriprion.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class DescriptionFragment: Fragment(), CoroutineScope by MainScope() {

    private var _binding: FragmentDescriprionBinding? = null
    private val binding get() = _binding!!


    private lateinit var movies: MoviesDataTransfer
    private val viewModel: DescriptionViewModel by lazy {
        ViewModelProvider(this).get(DescriptionViewModel::class.java)
    }

    private val repository: Repository = RepoImpl()

    private fun renderData(moviesDataTransfer: MoviesDataTransfer) {
        with(binding) {

            val title = moviesDataTransfer.title.toString()
            val overview = moviesDataTransfer.overview.toString()
            val rating = moviesDataTransfer.vote_average.toString()
            val date = moviesDataTransfer.release_date.toString()
            val tagline = moviesDataTransfer.tagline

            title.let {
                descTitleTextView.text = title
                descDescTextView.text = overview
                descRatingTextView.text = rating
                releaseDateTextView.text = date
                descTaglineTextView.text = tagline
            }
        }
    }

    private fun convertMoviesToEntity(movie: MoviesDataTransfer): NotesEntity {
        return NotesEntity(0,
            movie.id ?: 0,
            movie.note ?: "")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        movies = arguments?.getParcelable(BUNDLE_TAG) ?: MoviesDataTransfer()
        noteTextView?.setOnClickListener {
            DescriptionSection.visibility = View.GONE
            inputLinear?.visibility = View.VISIBLE
        }

        noteEditText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN){

                val note: String = (noteEditText as TextView).text.toString()
                movies.note = note
                CoroutineScope(Dispatchers.IO).launch {
                    Database.db.notesDao().insert(convertMoviesToEntity(movies))
                }

                noteTextView?.text = note
                v.hideKeyboard()
                DescriptionSection.visibility = View.VISIBLE
                inputLinear?.visibility = View.GONE
                return@OnKeyListener true
            }
            false
        })

        button_bookmark?.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (repository.checkBookmarkExist(movies.id)) {
                    button_bookmark.setImageResource(R.drawable.ic_bookmark_done)
                    repository.removeBookmark(movies)
                } else {
                    repository.insertBookmark(movies)
                    button_bookmark.setImageResource(R.drawable.ic_bookmark)
                }
            }
        }

        with(binding) {
            val movieId = movies.id
            viewModel.liveData.observe(viewLifecycleOwner, { appState ->
                when (appState) {
                    is ApplicationState.Error -> {
                        //todo
                    }
                    ApplicationState.Loading -> {
                        //todo
                    }
                    is ApplicationState.Success -> {
                        val genreList: @RawValue List<Genre>? = appState.movieData.genres
                        val sb = StringBuffer()
                        sb.append(genreList?.get(0)?.name)
                        if (genreList != null) {
                            for (i in 1 until genreList.size) {
                                sb.append(", ")
                                sb.append(genreList[i].name)
                            }
                        }
                        descGenreTextView.text = sb.toString()
                        releaseDateTextView.text = appState.movieData.release_date
                        descTitleTextView.text = appState.movieData.title
                        descDescTextView.text = appState.movieData.overview
                        descRatingTextView.text = appState.movieData.vote_average.toString()
                        descTaglineTextView.text = appState.movieData.tagline
                        Picasso.get()
                            .load(BASE_URL_IMG.plus("${appState.movieData.backdrop_path}"))
                            .placeholder(R.drawable.ic_movie_yellow)
                            .fit().centerCrop()
                            .into(descImageView)
                        CoroutineScope(Dispatchers.IO).launch {
                        noteTextView?.text = repository.getNote(movieId).note
                        }
                    }
                }
            })
            viewModel.loadData(movieId)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
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