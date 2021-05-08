package com.mirjanakopanja.movieapp.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.databinding.MainFragmentBinding
import com.mirjanakopanja.movieapp.model.Movies
import com.mirjanakopanja.movieapp.ui.main.MainViewModel
import com.mirjanakopanja.movieapp.ui.main.MoviesAdapter

class MainFragment : Fragment() {
    private  var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private val adapter = MoviesAdapter(object : OnItemClick{
        override fun onItemClick(movies: Movies) {
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
                              savedInstanceState: Bundle?): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            processData(it)
        })
        viewModel.getMovieFromLocalSource()
    }

    private fun processData(appState: ApplicationState) {
        when(appState){

            is ApplicationState.Success -> {
                binding.loadingFrameLayout.visibility = View.GONE
                adapter.setMovieList(appState.movie)
            }

            is ApplicationState.Loading -> {
                binding.loadingFrameLayout.visibility = View.VISIBLE
            }

            is ApplicationState.Error -> {
                binding.loadingFrameLayout.visibility = View.GONE
                Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    interface OnItemClick{
        fun onItemClick(movies: Movies)
    }
}