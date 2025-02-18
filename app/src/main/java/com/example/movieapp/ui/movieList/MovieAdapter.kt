package com.example.movieapp.ui.movieList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.local.Movie
import com.example.movieapp.databinding.ItemMovieBinding

class MovieAdapter(
    private val onMovieClick: (Movie) -> Unit,
    private val onFavoriteClick: (Movie) -> Unit,
    private val onEditClick: (Movie) -> Unit,
    private val isFavoriteFragment: Boolean // Determines when to show edit button
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.titleEditText.setText(movie.title)
            binding.releaseDateEditText.setText(movie.release_date)
            binding.ratingEditText.setText(movie.vote_average.toString())

            Glide.with(binding.root)
                .load("https://image.tmdb.org/t/p/w500" + movie.posterPath)
                .into(binding.posterImageView)

            updateFavoriteIcon(movie.favorite)

            binding.root.setOnClickListener { onMovieClick(movie) }

            binding.favoriteButton.setOnClickListener {
                val updatedMovie = movie.copy(favorite = !movie.favorite)
                onFavoriteClick(updatedMovie)
                updateFavoriteIcon(updatedMovie.favorite)
            }

            // ✅ Show "Edit" button only in Favorite Fragment
            binding.editButton.visibility = if (isFavoriteFragment) View.VISIBLE else View.GONE

            binding.editButton.setOnClickListener {
                enableEditing(movie, true) // ✅ Pass `movie` as a parameter
            }

            // ✅ Save changes when user clicks outside the EditText
            binding.titleEditText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) saveChanges(movie)
            }
            binding.releaseDateEditText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) saveChanges(movie)
            }
            binding.ratingEditText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) saveChanges(movie)
            }
        }

        // ✅ Pass `movie` as a parameter to fix the error
        private fun enableEditing(movie: Movie, enable: Boolean) {
            binding.titleEditText.isFocusable = enable
            binding.titleEditText.isFocusableInTouchMode = enable

            binding.releaseDateEditText.isFocusable = enable
            binding.releaseDateEditText.isFocusableInTouchMode = enable

            binding.ratingEditText.isFocusable = enable
            binding.ratingEditText.isFocusableInTouchMode = enable

            if (!enable) saveChanges(movie)
        }

        private fun saveChanges(movie: Movie) {
            val newTitle = binding.titleEditText.text.toString()
            val newReleaseDate = binding.releaseDateEditText.text.toString()
            val newVoteAverage = binding.ratingEditText.text.toString().toDoubleOrNull() ?: movie.vote_average

            if (newTitle != movie.title || newReleaseDate != movie.release_date || newVoteAverage != movie.vote_average) {
                val updatedMovie = movie.copy(
                    title = newTitle,
                    release_date = newReleaseDate,
                    vote_average = newVoteAverage
                )
                onEditClick(updatedMovie)
            }
        }

        private fun updateFavoriteIcon(isFavorite: Boolean) {
            binding.favoriteButton.setImageResource(
                if (isFavorite) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )
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
