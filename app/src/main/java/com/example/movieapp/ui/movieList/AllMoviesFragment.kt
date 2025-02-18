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
import com.example.movieapp.utils.autoCleared
import com.example.movieapp.utils.Resource
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
            originalList // אם השדה ריק - החזר את הרשימה המלאה
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