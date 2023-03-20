package com.mirjanakopanja.movieapp.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.data.Movies
import com.mirjanakopanja.movieapp.databinding.MovieItemSearchBinding
import com.mirjanakopanja.movieapp.extensions.BASE_URL_IMG
import com.squareup.picasso.Picasso

class SearchAdapter(
    private var onItemViewClickListener: SearchFragment.OnItemClick?
): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var movies: Movies = Movies()
    private lateinit var binding: MovieItemSearchBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = MovieItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    fun seListItemsSearch(movieList: Movies) {
        this.movies = movieList
        notifyDataSetChanged()
    }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.titleSearch.text = movies.results?.get(position)?.title

        val ratingDouble: Double? = movies.results?.get(position)?.vote_average?.toDouble()
        val ratingFloat = ratingDouble?.toFloat()
        holder.ratingSearch.rating = ratingFloat?.div(2)!!

        Picasso.get()
            .load(BASE_URL_IMG.plus(movies.results?.get(position)?.poster_path))
            .placeholder(R.drawable.ic_movie_yellow)
            .fit().centerCrop()
            .into(holder.imageSearch)

        holder.itemView.setOnClickListener{
            onItemViewClickListener?.onItemClick(movies.results?.get(position))
        }
    }

    override fun getItemCount(): Int = movies.results?.size!!

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val titleSearch: TextView = binding.movieItemTitleSearch
        val ratingSearch: RatingBar = binding.ratingBarSearch
        val imageSearch: ImageView = binding.movieItemImageSearch
    }
}