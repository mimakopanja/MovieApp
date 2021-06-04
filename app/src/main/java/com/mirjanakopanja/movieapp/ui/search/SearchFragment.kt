package com.mirjanakopanja.movieapp.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.data.Movies
import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.databinding.FragmentSearchBinding
import com.mirjanakopanja.movieapp.model.api.MoviesAPI
import com.mirjanakopanja.movieapp.ui.description.DescriptionFragment
import kotlinx.android.synthetic.main.fragment_descriprion.*
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment: Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private var searchString: String = ""


    private val adapterSearch = SearchAdapter(object : OnItemClick {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerCategories.adapter = adapterSearch

        search_button.setOnClickListener {
            loadSearchResults()
        }
        movieSearchTextView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                loadSearchResults()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun loadSearchResults() {
        searchString = binding.movieSearchTextView.text.toString()
        val movieApi = MoviesAPI.create().searchMovies(query = searchString)
        movieApi.enqueue(object : Callback<Movies> {
            override fun onResponse(
                call: Call<Movies>, response: Response<Movies>
            ) {
                if (response.body() != null) {
                    adapterSearch.seListItemsSearch(response.body()!!)
                }
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    interface OnItemClick{
        fun onItemClick(movies: MoviesDataTransfer?)
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}