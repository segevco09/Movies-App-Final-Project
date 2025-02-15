package com.example.movieapp.ui.movieList

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.ui.MovieViewModel
import com.example.movieapp.utils.Loading
import com.example.movieapp.utils.Resource
import com.example.movieapp.utils.Success
import com.example.movieapp.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.data.local.Movie
import com.google.android.material.floatingactionbutton.FloatingActionButton

@AndroidEntryPoint
class AllMoviesFragment : Fragment(R.layout.fragment_all_movies) {
    private val viewModel: MovieViewModel by viewModels()
    private var adapter by autoCleared<MovieAdapter>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieAdapter({ movie ->
            findNavController().navigate(R.id.action_allMoviesFragment_to_movieDetailFragment, Bundle().apply { putParcelable("movie", movie) })
        }) { movie ->
            viewModel.updateFavorite(movie.id, !movie.favorite)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val favoriteButton = view.findViewById<FloatingActionButton>(R.id.favoritesButton)
        favoriteButton.setOnClickListener {
            findNavController().navigate(R.id.action_allMoviesFragment_to_favoriteMoviesFragment)
        }

        viewModel.movies.observe(viewLifecycleOwner) {
            when (it.status) {
                is Success -> adapter.submitList(it.status.data)
                is Error -> Toast.makeText(context, it.status.message, Toast.LENGTH_SHORT).show()
                is Loading -> {/* Show loading state */}
                is com.example.movieapp.utils.Error -> Toast.makeText(context, it.status.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}


