package com.mirjanakopanja.movieapp.ui.description


import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.transition.Hold
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.data.Genre
import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.data.cast.CastCredits
import com.mirjanakopanja.movieapp.databinding.FragmentDescriprionBinding
import com.mirjanakopanja.movieapp.db.Database
import com.mirjanakopanja.movieapp.db.NotesEntity
import com.mirjanakopanja.movieapp.extensions.*
import com.mirjanakopanja.movieapp.model.api.MoviesAPI
import com.mirjanakopanja.movieapp.model.repository.RepoImpl
import com.mirjanakopanja.movieapp.model.repository.Repository
import com.mirjanakopanja.movieapp.ui.description.cast.ActorFragment
import com.mirjanakopanja.movieapp.ui.description.cast.CastRecyclerViewAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.parcel.RawValue
import kotlinx.android.synthetic.main.fragment_descriprion.*
import kotlinx.android.synthetic.main.item_cast.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DescriptionFragment: Fragment(), CoroutineScope by MainScope() {


    private var _binding: FragmentDescriprionBinding? = null
    private val binding get() = _binding!!

    private lateinit var movies: MoviesDataTransfer
    private val viewModel: DescriptionViewModel by lazy {
        ViewModelProvider(this).get(DescriptionViewModel::class.java)
    }

    private val castAdapter = CastRecyclerViewAdapter(object: OnItemClick{
        override fun onItemClick(actorId: Int?) {
            val manager = activity?.supportFragmentManager
            if (manager != null){
                val bundle = Bundle()
                if (actorId != null) {
                    bundle.putInt(BUNDLE_CAST, actorId)
                }
                manager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                    .replace(R.id.container, ActorFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })

    private val repository: Repository = RepoImpl()

    private fun convertMoviesToEntity(movie: MoviesDataTransfer): NotesEntity {
        return NotesEntity(0,
            movie.id ?: 0,
            movie.note ?: "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        movies = arguments?.getParcelable(BUNDLE_TAG) ?: MoviesDataTransfer()
        binding.recyclerViewCast?.adapter = castAdapter

        noteTextView?.setOnClickListener {
            showDescriptionSection.visibility = View.GONE
//            inputLinear?.visibility = View.VISIBLE
        }
        loadNote()
        loadBookmark()
        loadData()
        loadVideo()
    }

    private fun loadVideo() {
        viewModel.getVideo(movies.id)
        binding.playBtn?.setOnClickListener {
            if (viewModel.videos.value != null && viewModel.videos.value?.size != 0) {
                val videoDialog = viewModel.videos.value?.key?.let { it1 -> VideoPlayerDialog(it1) }
                videoDialog?.show(childFragmentManager, "Video Dialog")
            }
            else {
                showToast("Video not found!")
            }
        }
    }

    private fun loadData() {
        with(binding) {
            val movieId = movies.id
            viewModel.liveData.observe(viewLifecycleOwner, { appState ->
                when (appState) {
                    is ApplicationState.Error -> {
                        showToast("Error Cast")
                    }
                    ApplicationState.Loading -> {
                        //todo
                    }
                    is ApplicationState.Success -> {
                        val genreList: @RawValue List<Genre>? = appState.data.genres
                        val sb = StringBuffer()
                        sb.append(genreList?.get(0)?.name)
                        if (genreList != null) {
                            for (i in 1 until genreList.size) {
                                sb.append(", ")
                                sb.append(genreList[i].name)
                            }
                        }
                        descGenreTextView?.text = sb.toString()
                        releaseDateTextView?.text = appState.data.release_date
                        descTitleTextView?.text = appState.data.title
                        descDescTextView?.text = appState.data.overview
                        descRatingTextView?.text = appState.data.vote_average.toString().plus(" / 10")
                        descTaglineTextView?.text = appState.data.tagline
                        Picasso.get()
                            .load(BASE_URL_IMG.plus("${appState.data.backdrop_path}"))
                            .placeholder(R.drawable.ic_movie_yellow)
                            .fit().centerCrop()
                            .into(descImageView)
                        CoroutineScope(Dispatchers.IO).launch {
                            noteTextView?.text = repository.getNote(movieId).note
                        }
                    }
                    is ApplicationState.SuccessCast -> {
                        val moviesApi = MoviesAPI.create().getMovieCredits(movieId)
                        moviesApi.enqueue(object : Callback<CastCredits> {
                            override fun onResponse(
                                call: Call<CastCredits>,
                                response: Response<CastCredits>
                            ) {
                                if (response.body() != null) {
                                    castAdapter.updateCastAndCrew(response.body()!!)
                                }
                            }

                            override fun onFailure(call: Call<CastCredits>, t: Throwable) {
                                t.printStackTrace()
                            }

                        })
                }
                }
            })
            viewModel.loadData(movieId)
        }
    }

    private fun loadBookmark() {
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
    }

    private fun loadNote() {
        Thread {
            noteEditText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {

                    val note: String = (noteEditText as TextView).text.toString()
                    movies.note = note
                    CoroutineScope(Dispatchers.IO).launch {
                        Database.db.notesDao().insert(convertMoviesToEntity(movies))
                    }
                    noteTextView?.text = note
                    v.hideKeyboard()
                    showDescriptionSection.visibility = View.VISIBLE
                    inputLinear?.visibility = View.GONE
                    return@OnKeyListener true
                }
                false
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentDescriprionBinding.inflate(inflater, container, false)
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

    private fun View.hideKeyboard() {
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

    interface OnItemClick {
        fun onItemClick(actorId: Int?)
    }
}