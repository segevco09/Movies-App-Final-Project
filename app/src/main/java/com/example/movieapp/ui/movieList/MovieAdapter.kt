package com.example.movieapp.ui.movieList

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.local.Movie
import com.example.movieapp.databinding.DialogEditMovieBinding
import com.example.movieapp.databinding.ItemMovieBinding

class MovieAdapter(
    private val onMovieClick: (Movie) -> Unit,
    private val onFavoriteClick: (Movie) -> Unit,
    private val onEditClick: (Movie) -> Unit,
    private val isFavoriteFragment: Boolean
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

            // Show "Edit" button only in Favorite Fragment
            binding.editButton.visibility = if (isFavoriteFragment) View.VISIBLE else View.GONE

            binding.editButton.setOnClickListener {
                showEditDialog(movie)
            }
        }

        private fun showEditDialog(movie: Movie) {
            val dialogBinding = DialogEditMovieBinding.inflate(LayoutInflater.from(binding.root.context))

            // Set current values
            dialogBinding.editMovieTitle.setText(movie.title)
            dialogBinding.editMovieReleaseDate.setText(movie.release_date)
            dialogBinding.editMovieRating.setText(movie.vote_average.toString())

            val alertDialog = AlertDialog.Builder(binding.root.context)
                .setView(dialogBinding.root)
                .setTitle("Edit Movie")
                .setCancelable(true) // ✅ Allows clicking outside to dismiss
                .create()

            dialogBinding.confirmChangesButton.setOnClickListener {
                val newTitle = dialogBinding.editMovieTitle.text.toString().trim()
                val newReleaseDate = dialogBinding.editMovieReleaseDate.text.toString().trim()
                val newVoteAverage = dialogBinding.editMovieRating.text.toString().toDoubleOrNull()

                // ✅ Validation checks
                if (newTitle.isEmpty()) {
                    dialogBinding.editMovieTitle.error = "Title cannot be empty"
                    return@setOnClickListener
                }

                if (newReleaseDate.isEmpty()) {
                    dialogBinding.editMovieReleaseDate.error = "Release date cannot be empty"
                    return@setOnClickListener
                }

                if (newVoteAverage == null || newVoteAverage < 0 || newVoteAverage > 10) {
                    dialogBinding.editMovieRating.error = "Rating must be between 0 and 10"
                    return@setOnClickListener
                }

                // ✅ If validation passes, update the movie
                val updatedMovie = movie.copy(
                    title = newTitle,
                    release_date = newReleaseDate,
                    vote_average = newVoteAverage
                )

                onEditClick(updatedMovie) // ✅ Save changes
                alertDialog.dismiss() // ✅ Close dialog only if valid
            }

            dialogBinding.cancelButton.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
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