package com.mirjanakopanja.movieapp.ui.description.cast

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.mirjanakopanja.movieapp.BuildConfig
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.data.cast.Actor
import com.mirjanakopanja.movieapp.databinding.FragmentActorBinding
import com.mirjanakopanja.movieapp.extensions.BASE_URL_IMG
import com.mirjanakopanja.movieapp.extensions.BIRTHPLACE_BUNDLE
import com.mirjanakopanja.movieapp.extensions.BUNDLE_CAST
import com.mirjanakopanja.movieapp.model.api.MoviesAPI
import com.mirjanakopanja.movieapp.ui.maps.MapsFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_actor.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActorFragment: Fragment() {

    private var _binding : FragmentActorBinding? = null
    private val binding get() = _binding
    private lateinit var actorName : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActorBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actorId = arguments?.getInt(BUNDLE_CAST)
        val movieApi = MoviesAPI.create().getActor(actorId, BuildConfig.api_key)

        movieApi.enqueue(object : Callback<Actor> {
            override fun onResponse(call: Call<Actor>, response: Response<Actor>) {
                if (response.body() != null) {
                    actorName = response.body()?.name.toString()
                    binding?.actorName?.text = response.body()?.name
                    binding?.actorBirthday?.text = response.body()?.birthday
                    binding?.placeOfBirth?.text = response.body()?.place_of_birth
                    binding?.actorBio?.text = response.body()?.biography
                    Picasso.get()
                        .load(BASE_URL_IMG.plus("${response.body()?.profile_path}"))
                        .placeholder(R.drawable.ic_movie_yellow)
                        .fit().centerCrop()
                        .into(binding?.actorPhoto)
                }
            }
            override fun onFailure(call: Call<Actor>, t: Throwable) {
                t.printStackTrace()
            }

        })

        placeOfBirth.setOnClickListener {
            val manager = activity?.supportFragmentManager
            val bundle = Bundle()
            bundle.putString(BIRTHPLACE_BUNDLE, placeOfBirth.text.toString())
            manager?.beginTransaction()
                ?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                ?.replace(R.id.container, MapsFragment.newInstance(bundle))
                ?.addToBackStack("")
                ?.commitAllowingStateLoss()
        }

    }

    companion object{
        fun newInstance(bundle: Bundle): ActorFragment {
            val fragment = ActorFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}