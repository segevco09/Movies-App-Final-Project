package com.example.movieapp.ui.movieList

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentAllMoviesBinding
import com.example.movieapp.ui.MovieViewModel
import com.example.movieapp.utils.autoCleared
import com.example.movieapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllMoviesFragment : Fragment(R.layout.fragment_all_movies) {
    private val viewModel: MovieViewModel by viewModels()
    private var adapter by autoCleared<MovieAdapter>()

    private var _binding: FragmentAllMoviesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllMoviesBinding.bind(view)

        adapter = MovieAdapter(
            onMovieClick = { movie ->
                findNavController().navigate(
                    R.id.action_allMoviesFragment_to_movieDetailFragment,
                    Bundle().apply { putParcelable("movie", movie) }
                )
            },
            onFavoriteClick = { movie ->
                viewModel.updateFavorite(movie)
            },
            onEditClick = { movie ->
                // ✅ No edits allowed here, but we must pass it to the adapter
                viewModel.updateMovie(movie)
            },
            isFavoriteFragment = false // ✅ Editing is disabled in this fragment
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        viewModel.popularMovies.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (!it.data.isNullOrEmpty()) {
                        adapter.submitList(it.data)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> { /* Show loading indicator if needed */ }
            }
        }
    }

    fun filterMovies(query: String) {
        val originalList = viewModel.popularMovies.value?.data ?: emptyList()

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
        val originalList = viewModel.popularMovies.value?.data ?: emptyList()
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
    override fun onResume() {
        super.onResume()
        viewModel.fetchPopularMovies() // ✅ Forces fresh data from API every time you come back
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
