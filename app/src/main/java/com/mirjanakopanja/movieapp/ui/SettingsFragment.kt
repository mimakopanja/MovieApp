package com.mirjanakopanja.movieapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mirjanakopanja.movieapp.databinding.FragmentSettingsBinding
import com.mirjanakopanja.movieapp.extensions.ADULT_PREF_KEY
import com.mirjanakopanja.movieapp.extensions.SHARED_PREF_KEY
import com.mirjanakopanja.movieapp.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchAdult.setOnCheckedChangeListener { buttonView, isChecked -> loadSharedDataSwitch()}
            initSharedDataSwitch()
            loadSharedDataSwitch()
    }

    private fun loadSharedDataSwitch() {
        if (!switchAdult.isChecked){
            viewModel.getNonAdultMovies()
        } else{
            viewModel.getAdultMovies()
        }
        saveDataToSharedPreference()
    }

    private fun initSharedDataSwitch() {
        activity?.let {
            switchAdult.isChecked = it.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
                .getBoolean(ADULT_PREF_KEY, true)
        }
    }

    private fun saveDataToSharedPreference() {
        activity?.let {
            val preferences = it.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putBoolean(ADULT_PREF_KEY, switchAdult.isChecked)
            editor.apply()
        }

    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}