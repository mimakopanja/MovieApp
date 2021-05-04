package com.mirjanakopanja.movieapp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.mirjanakopanja.movieapp.ApplicationState
import com.mirjanakopanja.movieapp.databinding.MainFragmentBinding

class MainFragment : Fragment() {
    private  var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private val adapter = MoviesAdapter()

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
            renderData(it)
        })
        viewModel.getMovieFromLocalSource()
    }

    private fun renderData(appState: ApplicationState) {
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
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }
}