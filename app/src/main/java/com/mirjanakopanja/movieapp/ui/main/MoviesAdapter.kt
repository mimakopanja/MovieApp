package com.mirjanakopanja.movieapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.databinding.MainActivityBinding
import com.mirjanakopanja.movieapp.databinding.MovieItemBinding
import com.mirjanakopanja.movieapp.model.Movies
import com.mirjanakopanja.movieapp.ui.fragments.MainFragment
import kotlinx.android.synthetic.main.fragment_descriprion.view.*


class MoviesAdapter(
    private var onItemViewClickListener: MainFragment.OnItemClick?) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private var movies: List<Movies> = listOf()
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
        holder.bind(movies[position])
    }

    fun setMovieList(data: List<Movies>){
        movies = data
        notifyDataSetChanged()
    }

    override fun getItemCount() =  movies.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(movie: Movies) = with(binding){
            movieItemTitle.text = movie.movie.title
            movieItemYear.text = movie.year.toString()
            movieItemImage.setImageResource(movie.image)
            itemView.setOnClickListener{
                onItemViewClickListener?.onItemClick(movie)
            }

        }
    }
}