package com.example.movieapp.ui.adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.local.Movie
import com.example.movieapp.databinding.DialogEditMovieBinding
import com.example.movieapp.databinding.ItemMovieBinding
import com.example.movieapp.utils.MovieDiffCallback
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
            binding.titleTextView.text = movie.title
            binding.releaseDateTextView.text = movie.releaseDate
            binding.ratingTextView.text = itemView.context.getString(
                R.string.rating_format,
                movie.voteAverage
            )

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
                    val updatedMovie = movie.copy(favorite = true)
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
            dialogBinding.editMovieReleaseDate.text = movie.releaseDate
            dialogBinding.editMovieRating.setText(
                itemView.context.getString(R.string.rating_format, movie.voteAverage)
            )

            val alertDialog = AlertDialog.Builder(binding.root.context)
                .setView(dialogBinding.root)
                .setTitle(binding.root.context.getString(R.string.edit_movie))
                .setCancelable(true) // Allows clicking outside to dismiss
                .create()

            // Open DatePicker when clicking the release date field
            dialogBinding.editMovieReleaseDate.setOnClickListener {
                showDatePickerDialog(dialogBinding.editMovieReleaseDate)
            }

            dialogBinding.confirmChangesButton.setOnClickListener {
                val newTitle = dialogBinding.editMovieTitle.text.toString().trim()
                val newReleaseDate = dialogBinding.editMovieReleaseDate.text.toString().trim()
                val newVoteAverage = dialogBinding.editMovieRating.text.toString().toDoubleOrNull()

                // Validation checks
                if (newTitle.isEmpty()) {
                    dialogBinding.editMovieTitle.error =
                        binding.root.context.getString(R.string.title_cannot_be_empty)
                    return@setOnClickListener
                }

                if (newReleaseDate.isEmpty()) {
                    Toast.makeText(binding.root.context, binding.root.context.getString(R.string.select_release_date), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (newVoteAverage == null || newVoteAverage < 0 || newVoteAverage > 10) {
                    dialogBinding.editMovieRating.error = binding.root.context.getString(R.string.rating_error_message)
                    return@setOnClickListener
                }

                // If validation passes, update the movie
                val updatedMovie = movie.copy(
                    title = newTitle,
                    releaseDate = newReleaseDate,
                    voteAverage = newVoteAverage
                )

                onEditClick(updatedMovie) // Save changes
                alertDialog.dismiss() // Close dialog only if valid
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
                    val formattedDate = String.format(
                        Locale.US,  // Using US locale for consistent YYYY-MM-DD format
                        "%04d-%02d-%02d",
                        selectedYear,
                        selectedMonth + 1,
                        selectedDay
                    )
                    textView.text = formattedDate
                },
                year, month, day
            )

            datePickerDialog.show()
        }

        private fun showRemoveFromFavoritesDialog(movie: Movie) {
            MaterialAlertDialogBuilder(binding.root.context, R.style.CustomAlertDialog)
                .setBackground(
                    AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.edit_text_background
                    )
                )
                .setTitle(binding.root.context.getString(R.string.dialog_remove_title))
                .setMessage(binding.root.context.getString(R.string.dialog_remove_message))
                .setPositiveButton(binding.root.context.getString(R.string.dialog_remove_button)) { dialog, _ ->
                    val updatedMovie = movie.copy(favorite = false)
                    onFavoriteClick(updatedMovie)
                    updateFavoriteIcon(false)
                    dialog.dismiss()
                }
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
