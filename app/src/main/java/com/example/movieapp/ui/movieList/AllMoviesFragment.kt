package com.example.movieapp.ui.movieList

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.data.local.Movie
import com.example.movieapp.databinding.FragmentAllMoviesBinding
import com.example.movieapp.ui.MovieViewModel
import com.example.movieapp.utils.Loading
import com.example.movieapp.utils.Resource
import com.example.movieapp.utils.Success
import com.example.movieapp.utils.autoCleared
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

        adapter = MovieAdapter({ movie ->
            findNavController().navigate(
                R.id.action_allMoviesFragment_to_movieDetailFragment,
                Bundle().apply { putParcelable("movie", movie) }
            )
        }) { movie ->
            viewModel.updateFavorite(movie)
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.favoritesButton.setOnClickListener {
            findNavController().navigate(R.id.action_allMoviesFragment_to_favoriteMoviesFragment)
        }

        viewModel.movies.observe(viewLifecycleOwner) {
            when (it.status) {
                is Success -> adapter.submitList(it.status.data)
                is com.example.movieapp.utils.Error ->
                    Toast.makeText(context, it.status.message, Toast.LENGTH_SHORT).show()
                is Loading -> {/* Show loading state */}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}