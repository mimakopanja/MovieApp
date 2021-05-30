package com.mirjanakopanja.movieapp.ui.main.popular

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.data.Movies
import com.mirjanakopanja.movieapp.databinding.ItemMovieBinding
import com.mirjanakopanja.movieapp.extensions.BASE_URL_IMG
import com.mirjanakopanja.movieapp.ui.main.MainFragment
import com.squareup.picasso.Picasso

class UpcomingMovieAdapter( private var onItemViewClickListener: MainFragment.OnItemClick?) :
    RecyclerView.Adapter<UpcomingMovieAdapter.ViewHolder>() {

    private lateinit var binding: ItemMovieBinding
    var movies: Movies = Movies()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.popularTitle.text = movies.results?.get(position)?.title


        val ratingDouble: Double? = movies.results?.get(position)?.vote_average?.toDouble()
        val ratingFloat = ratingDouble?.toFloat()
        holder.rating.rating = ratingFloat?.div(2)!!

        Picasso.get()
            .load(BASE_URL_IMG.plus(movies.results?.get(position)?.poster_path))
            .placeholder(R.drawable.ic_movie_yellow)
            .fit().centerCrop()
            .into(holder.popularImage)

        holder.itemView.setOnClickListener{
            onItemViewClickListener?.onItemClick(movies.results?.get(position))
        }
    }

    override fun getItemCount(): Int = movies.results?.size!!

    fun setMovieListItems(movieList: Movies){
        this.movies = movieList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val popularTitle: TextView = binding.titleMovie
        val popularImage: ImageView = binding.imageMovie
        val rating: RatingBar = binding.ratingBar
    }
}