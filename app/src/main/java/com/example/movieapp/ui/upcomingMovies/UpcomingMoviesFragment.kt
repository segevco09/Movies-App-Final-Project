package com.example.movieapp.ui.upcomingMovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentUpcomingMoviesBinding
import com.example.movieapp.ui.MovieViewModel
import com.example.movieapp.utils.Resource
import com.example.movieapp.ui.movieList.MovieAdapter
import com.example.movieapp.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingMoviesFragment : Fragment() {
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    private var _binding: FragmentUpcomingMoviesBinding by autoCleared()
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieAdapter(
            onMovieClick = { movie ->
                findNavController().navigate(
                    UpcomingMoviesFragmentDirections.actionUpcomingMoviesFragmentToMovieDetailFragment(movie)
                )
            },
            onFavoriteClick = { movie ->
                viewModel.updateFavorite(movie) // Toggle favorite status
            },
            onEditClick = { movie ->
                viewModel.updateMovie(movie) // Edits will only persist if the movie is favorite
            },
            isFavoriteFragment = false // Editing is disabled here
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.upcomingMovies.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    adapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    Toast.makeText(context, getString(R.string.loading), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchUpcomingMovies() // Triggers API fetch when returning
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
        val originalList = viewModel.upcomingMovies.value?.data ?: emptyList()
        val sortedList = when (sortType) {
            "High Rate" -> originalList.sortedByDescending { it.vote_average }
            "Low Rate" -> originalList.sortedBy { it.vote_average }
            "Latest" -> originalList.sortedByDescending { it.release_date }
            "Oldest" -> originalList.sortedBy { it.release_date }
            else -> originalList // Default - Regular
        }
        adapter.submitList(sortedList)
        binding.recyclerView.post {
            binding.recyclerView.scrollToPosition(0)
        }
    }

}
