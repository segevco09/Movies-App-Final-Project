package com.example.movieapp.ui.movieList

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.local.Movie
import com.example.movieapp.databinding.DialogEditMovieBinding
import com.example.movieapp.databinding.ItemMovieBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class MovieAdapter(
    private val onMovieClick: (Movie) -> Unit,
    private val onFavoriteClick: (Movie) -> Unit,
    private val onEditClick: (Movie) -> Unit,
    private val isFavoriteFragment: Boolean
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.titleTextView.setText(movie.title)
            binding.releaseDateTextView.setText(movie.release_date)
            binding.ratingTextView.setText(movie.vote_average.toString())

            Glide.with(binding.root)
                .load("https://image.tmdb.org/t/p/w500" + movie.posterPath)
                .into(binding.posterImageView)

            updateFavoriteIcon(movie.favorite)

            binding.root.setOnClickListener { onMovieClick(movie) }

            binding.favoriteButton.setOnClickListener {
                if (movie.favorite) {
                    // Show confirmation dialog only when removing from favorites in FavoriteFragment
                    showRemoveFromFavoritesDialog(movie)
                } else {
                    // Regular favorite toggle for non-favorite items or in other fragments
                    val updatedMovie = movie.copy(favorite = !movie.favorite)
                    onFavoriteClick(updatedMovie)
                    updateFavoriteIcon(updatedMovie.favorite)
                }
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
            dialogBinding.editMovieReleaseDate.text = movie.release_date // Use TextView now
            dialogBinding.editMovieRating.setText(movie.vote_average.toString())

            val alertDialog = AlertDialog.Builder(binding.root.context)
                .setView(dialogBinding.root)
                .setTitle("Edit Movie")
                .setCancelable(true) // ✅ Allows clicking outside to dismiss
                .create()

            // ✅ Open DatePicker when clicking the release date field
            dialogBinding.editMovieReleaseDate.setOnClickListener {
                showDatePickerDialog(dialogBinding.editMovieReleaseDate)
            }

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
                    Toast.makeText(binding.root.context, "Please select a release date", Toast.LENGTH_SHORT).show()
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


            alertDialog.show()
        }

        private fun showDatePickerDialog(textView: android.widget.TextView) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                textView.context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                    textView.text = formattedDate
                },
                year, month, day
            )

            datePickerDialog.show()
        }

        private fun showRemoveFromFavoritesDialog(movie: Movie) {
            MaterialAlertDialogBuilder(binding.root.context, R.style.CustomAlertDialog)
                .setBackground(binding.root.context.getDrawable(R.drawable.edit_text_background))
                .setTitle("Remove from Favorites")
                .setMessage("Are you sure you want to remove this movie from the favorite list? Your changes will not be saved.")
                .setPositiveButton("Remove") { dialog, _ ->
                    val updatedMovie = movie.copy(favorite = false)
                    onFavoriteClick(updatedMovie)
                    updateFavoriteIcon(false)
                    dialog.dismiss()
                }
//                .setNegativeButton("Cancel") { dialog, _ ->
//                    dialog.dismiss()
//                }
                .show()
                .apply {
                    // Style the dialog text colors
                    getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.getColor(R.color.yellow))
                    getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(context.getColor(R.color.light_gray))
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
