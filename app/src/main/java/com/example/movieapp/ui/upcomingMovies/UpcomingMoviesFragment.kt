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
import com.example.movieapp.databinding.FragmentUpcomingMoviesBinding
import com.example.movieapp.ui.MovieViewModel
import com.example.movieapp.utils.Resource
import com.example.movieapp.ui.movieList.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingMoviesFragment : Fragment() {
    private var _binding: FragmentUpcomingMoviesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieAdapter({ movie ->
            findNavController().navigate(
                UpcomingMoviesFragmentDirections.actionUpcomingMoviesFragmentToMovieDetailFragment(movie)
            )
        }) { movie ->
            viewModel.updateFavorite(movie)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.upcomingMovies.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> adapter.submitList(resource.data) //
                is Resource.Error -> Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                is Resource.Loading -> {/* Show loading state */}
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
