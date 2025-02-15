package com.example.movieapp.ui.movieList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.local.Movie
import com.example.movieapp.databinding.ItemMovieBinding

class MovieAdapter(private val onMovieClick: (Movie) -> Unit) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {
    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            itemView.findViewById<TextView>(R.id.titleTextView).text = movie.title
            itemView.findViewById<TextView>(R.id.releaseDateTextView).text = movie.release_date
            Glide.with(itemView).load("https://image.tmdb.org/t/p/w500" + movie.posterPath).into(itemView.findViewById(R.id.posterImageView))
            itemView.setOnClickListener { onMovieClick(movie) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = holder.bind(getItem(position))
}