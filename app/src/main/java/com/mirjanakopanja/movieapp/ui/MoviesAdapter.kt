package com.mirjanakopanja.movieapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.data.Movies
import com.mirjanakopanja.movieapp.data.MoviesDataTransfer
import com.mirjanakopanja.movieapp.databinding.MovieItemBinding
import com.mirjanakopanja.movieapp.extensions.BASE_URL_IMG
import com.mirjanakopanja.movieapp.ui.main.MainFragment
import com.squareup.picasso.Picasso


class MoviesAdapter(
    private var onItemViewClickListener: MainFragment.OnItemClick?) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    var movies : Movies = Movies()
    private lateinit var binding: MovieItemBinding

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    fun removeListener(){
        onItemViewClickListener = null
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = movies.results?.get(position)?.title
        holder.year.text = movies.results?.get(position)?.release_date

        Picasso.get()
            .load(BASE_URL_IMG.plus(movies.results?.get(position)?.poster_path))
            .placeholder(R.drawable.ic_movie_yellow)
            .fit().centerCrop()
            .into(holder.image)

        holder.itemView.setOnClickListener{
            onItemViewClickListener?.onItemClick(movies.results?.get(position))
        }

    }

    fun setMovieListItems(movieList: Movies){
        this.movies = movieList
        notifyDataSetChanged()
    }

    override fun getItemCount() =  movies.results?.size!!
    fun setMovieList(movieData: MoviesDataTransfer) {
        this.movies.results = listOf(movieData)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = binding.movieItemTitle
        val year: TextView = binding.movieItemYear
        val image : ImageView = binding.movieItemImage
    }
}