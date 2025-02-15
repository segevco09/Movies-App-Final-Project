package com.example.movieapp.ui.movieDetail

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.local.Movie
import com.example.movieapp.databinding.FragmentMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = arguments?.getParcelable<Movie>("movie")
        view.findViewById<TextView>(R.id.titleTextView).text = movie?.title
        view.findViewById<TextView>(R.id.overviewTextView).text = movie?.overview
        view.findViewById<TextView>(R.id.releaseDateTextView)
        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + movie?.posterPath).into(view.findViewById(R.id.posterImageView))
    }
}