package com.mirjanakopanja.movieapp.ui.tv_shows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.data.tv.TvShows
import com.mirjanakopanja.movieapp.databinding.ItemMovieBinding
import com.mirjanakopanja.movieapp.databinding.MovieItemSearchBinding
import com.mirjanakopanja.movieapp.extensions.App
import com.mirjanakopanja.movieapp.extensions.BASE_URL_IMG
import com.squareup.picasso.Picasso

class TvShowsAdapter(
    private var onItemViewClickListener: ShowsFragment.OnItemClick?
    ):
    RecyclerView.Adapter<TvShowsAdapter.ViewHolder>(){

        private lateinit var binding: MovieItemSearchBinding
        var shows: TvShows = TvShows()
        private var lastPosition: Int = -1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            binding = MovieItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding.root)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.popularTitle.text = shows.results?.get(position)?.name
            val animation: Animation = AnimationUtils.loadAnimation(
                App.appInstance.applicationContext,
                if (position > lastPosition){
                    R.anim.up_from_bottom
                } else R.anim.down_from_top
            )
            holder.itemView.startAnimation(animation)
            lastPosition = position

            val ratingDouble: Double? = shows.results?.get(position)?.vote_average
            val ratingFloat = ratingDouble?.toFloat()
            holder.rating.rating = ratingFloat?.div(2)!!

            Picasso.get()
                .load(BASE_URL_IMG.plus(shows.results?.get(position)?.poster_path))
                .placeholder(R.drawable.ic_movie_yellow)
                .fit().centerCrop()
                .into(holder.popularImage)

            holder.itemView.setOnClickListener{
                onItemViewClickListener?.onItemClick(shows.results?.get(position))
            }
        }

        override fun getItemCount(): Int = shows.results?.size!!

        fun setMovieListItems(showList: TvShows){
            this.shows = showList
            notifyDataSetChanged()
        }

        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
            val popularTitle: TextView = binding.movieItemTitleSearch
            val popularImage: ImageView = binding.movieItemImageSearch
            val rating: RatingBar = binding.ratingBarSearch
        }
}