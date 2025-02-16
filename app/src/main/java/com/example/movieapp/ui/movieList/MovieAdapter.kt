package com.example.movieapp.ui.movieList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.local.Movie
import com.example.movieapp.databinding.ItemMovieBinding

class MovieAdapter(
    private val onMovieClick: (Movie) -> Unit,
    private val onFavoriteClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.titleTextView.text = movie.title
            binding.releaseDateTextView.text = movie.release_date
            binding.avgRateTextView.text = movie.vote_average.toString()

            Glide.with(binding.root)
                .load("https://image.tmdb.org/t/p/w500" + movie.posterPath)
                .into(binding.posterImageView)

            // Update favorite button appearance
            binding.favoriteButton.setImageResource(
                if (movie.favorite) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )

            binding.root.setOnClickListener { onMovieClick(movie) }

            binding.favoriteButton.setOnClickListener {
                val newFavoriteStatus = !movie.favorite
                onFavoriteClick(movie) // Pass the updated movie
                binding.favoriteButton.setImageResource(
                    if (newFavoriteStatus) R.drawable.baseline_favorite_24
                    else R.drawable.baseline_favorite_border_24
                ) // Update UI instantly
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}