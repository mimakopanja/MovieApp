package com.mirjanakopanja.movieapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.model.Movies


class MoviesAdapter() : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private var movies: List<Movies> = listOf()

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_item, parent,false) as View
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun setMovieList(data: List<Movies>){
        movies = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(movie: Movies){
            itemView.findViewById<TextView>(R.id.movie_item_title).text = movie.title
            itemView.findViewById<TextView>(R.id.movie_item_year).text = movie.year.toString()
            itemView.findViewById<ImageView>(R.id.movie_item_image).setImageResource(movie.image)
            itemView.setOnClickListener{
                Snackbar.make(itemView, movie.title, Snackbar.LENGTH_SHORT).show()
            }

        }
    }
}