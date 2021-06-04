package com.mirjanakopanja.movieapp.ui.tv_shows.show_description

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.data.tv.Season
import com.mirjanakopanja.movieapp.databinding.SeasonItemBinding
import com.mirjanakopanja.movieapp.extensions.App
import com.mirjanakopanja.movieapp.extensions.BASE_URL_IMG
import com.mirjanakopanja.movieapp.ui.description.DescriptionFragment
import com.squareup.picasso.Picasso
import kotlin.math.min

class SeasonsAdapter : RecyclerView.Adapter<SeasonsAdapter.ViewHolder>() {

    private var seasonsList = listOf<Season>()
    private lateinit var binding: SeasonItemBinding
    private var lastPosition: Int = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = SeasonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val seasonImage: ImageView = binding.seasonImage
        val seasonName: TextView = binding.seasonName
        val seasonDate: TextView = binding.seasonReleaseDate
        val episodes: TextView = binding.seasonEpisodes
    }

    fun updateSeasons(seasons: List<Season>) {
        this.seasonsList = seasons
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val animation: Animation = AnimationUtils.loadAnimation(
            App.appInstance.applicationContext,
            if (position > lastPosition){
                R.anim.down_from_top
            } else R.anim.up_from_bottom
        )
        holder.itemView.startAnimation(animation)
        lastPosition = position

        when (position) {
            0 -> {
                binding.spacingStart.visibility = View.VISIBLE
            }
            min(20, seasonsList.size) - 1 -> {
                binding.spacingEnd.visibility = View.VISIBLE
            }
            else -> {
                binding.spacingEnd.visibility = View.GONE
                binding.spacingStart.visibility = View.GONE
            }
        }
        Picasso.get()
            .load(BASE_URL_IMG.plus(seasonsList[position].poster_path))
            .placeholder(R.drawable.ic_person_yellow)
            .fit().centerCrop()
            .into(holder.seasonImage)
        holder.episodes.text = seasonsList[position].episode_count.toString().plus(" episodes")
        holder.seasonDate.text = seasonsList[position].air_date
        holder.seasonName.text = seasonsList[position].name
    }

    override fun getItemCount(): Int = min(20, seasonsList.size)
}