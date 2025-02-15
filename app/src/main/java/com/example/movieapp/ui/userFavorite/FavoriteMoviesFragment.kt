package com.example.movieapp.ui.userFavorite

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.ui.MovieViewModel
import com.example.movieapp.ui.movieList.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMoviesFragment : Fragment(R.layout.fragment_favorite_movies) {
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val emptyTextView = view.findViewById<TextView>(R.id.emptyTextView)

        adapter = MovieAdapter(
            onMovieClick = { movie ->
                findNavController().navigate(
                    R.id.action_favoriteMoviesFragment_to_movieDetailFragment,
                    Bundle().apply { putParcelable("movie", movie) }
                )
            },
            onFavoriteClick = { movie ->
                viewModel.updateFavorite(movie)
            }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.favoriteMovies.observe(viewLifecycleOwner) { movies ->
            adapter.submitList(movies)
            emptyTextView.visibility = if (movies.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}
