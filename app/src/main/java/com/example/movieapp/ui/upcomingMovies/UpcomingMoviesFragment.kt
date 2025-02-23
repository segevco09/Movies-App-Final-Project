package com.example.movieapp.ui.upcomingMovies

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentUpcomingMoviesBinding
import com.example.movieapp.ui.MovieViewModel
import com.example.movieapp.utils.Resource
import com.example.movieapp.ui.adapter.MovieAdapter
import com.example.movieapp.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingMoviesFragment : Fragment(R.layout.fragment_upcoming_movies) {
    private val viewModel: MovieViewModel by viewModels()
    private var adapter by autoCleared<MovieAdapter>()
    private var currentSort: String = ""

    private var _binding: FragmentUpcomingMoviesBinding by autoCleared()
    private val binding get() = _binding

    companion object {
        private const val KEY_CURRENT_SORT = "current_sort"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUpcomingMoviesBinding.bind(view)

        // Restore sort state or use default
        currentSort = savedInstanceState?.getString(KEY_CURRENT_SORT) 
            ?: getString(R.string.regular)

        adapter = MovieAdapter(
            onMovieClick = { movie ->
                findNavController().navigate(
                    UpcomingMoviesFragmentDirections.actionUpcomingMoviesFragmentToMovieDetailFragment(movie)
                )
            },
            onFavoriteClick = { movie ->
                viewModel.updateFavorite(movie)
            },
            onEditClick = { movie ->
                viewModel.updateMovie(movie)
            },
            isFavoriteFragment = false
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.upcomingMovies.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (!it.data.isNullOrEmpty()) {
                        val sortedList = when (currentSort) {
                            getString(R.string.high_rate) -> it.data.sortedByDescending { movie -> movie.voteAverage }
                            getString(R.string.low_rate) -> it.data.sortedBy { movie -> movie.voteAverage }
                            getString(R.string.latest) -> it.data.sortedByDescending { movie -> movie.releaseDate }
                            getString(R.string.oldest) -> it.data.sortedBy { movie -> movie.releaseDate }
                            else -> it.data
                        }
                        adapter.submitList(sortedList)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    // Do nothing during loading
                }
            }
        }
    }


    fun filterMovies(query: String) {
        val originalList = viewModel.upcomingMovies.value?.data ?: emptyList()
        val filteredList = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter { movie ->
                movie.title.contains(query, ignoreCase = true)
            }
        }
        adapter.submitList(filteredList)
    }

    fun sortMovies(sortType: String) {
        currentSort = sortType
        val originalList = viewModel.upcomingMovies.value?.data ?: emptyList()
        val sortedList = when (sortType) {
            getString(R.string.high_rate) -> originalList.sortedByDescending { it.voteAverage }
            getString(R.string.low_rate) -> originalList.sortedBy { it.voteAverage }
            getString(R.string.latest) -> originalList.sortedByDescending { it.releaseDate }
            getString(R.string.oldest) -> originalList.sortedBy { it.releaseDate }
            getString(R.string.regular) -> originalList
            else -> originalList
        }
        adapter.submitList(sortedList)
        binding.recyclerView.post {
            binding.recyclerView.scrollToPosition(0)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchUpcomingMovies()
    }

    // Add this method to save state
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_CURRENT_SORT, currentSort)
    }

}
