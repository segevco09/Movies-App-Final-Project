package com.example.movieapp.ui.userFavorite

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentFavoriteMoviesBinding
import com.example.movieapp.ui.MovieViewModel
import com.example.movieapp.ui.movieList.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMoviesFragment : Fragment(R.layout.fragment_favorite_movies) {
    private val viewModel: MovieViewModel by viewModels()
    private var _binding: FragmentFavoriteMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteMoviesBinding.bind(view)

        adapter = MovieAdapter(
            onMovieClick = { movie ->
                findNavController().navigate(
                    R.id.action_favoriteMoviesFragment_to_movieDetailFragment,
                    Bundle().apply { putParcelable("movie", movie) }
                )
            },
            onFavoriteClick = { movie ->
                viewModel.updateFavorite(movie) // ✅ Toggle favorite status
            },
            onEditClick = { movie ->
                viewModel.updateMovie(movie) // ✅ Saves user-edited movie details
            },
            isFavoriteFragment = true // ✅ Enables the "Edit" button
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.favoriteMovies.observe(viewLifecycleOwner) { movies ->
            adapter.submitList(movies)
            binding.emptyTextView.visibility = if (movies.isNullOrEmpty()) View.VISIBLE else View.GONE
        }
    }

    fun filterMovies(query: String) {
        val originalList = viewModel.favoriteMovies.value ?: emptyList()
        val filteredList = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter { movie ->
                movie.title.contains(query, ignoreCase = true)
            }
        }
        adapter.submitList(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
