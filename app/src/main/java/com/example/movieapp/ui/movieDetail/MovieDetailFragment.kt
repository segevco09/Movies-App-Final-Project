package com.example.movieapp.ui.movieDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.local.Movie
import com.example.movieapp.databinding.FragmentMovieDetailBinding
import com.example.movieapp.utils.Resource
import com.example.movieapp.utils.autoCleared
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding by autoCleared()
    private val binding get() = _binding

    private val viewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = args.movie.id

        // Use the new DetailViewModel
        viewModel.getMovieById(movieId).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.titleTextView.text = getString(R.string.loading)
                    binding.overviewTextView.text = getString(R.string.loading)
                }
                is Resource.Success -> {
                    resource.data?.let { updateUI(it) }
                }
                is Resource.Error -> {
                    Toast.makeText(context,
                        getString(R.string.failed_to_load_movie_details), Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Fetch trailer using the new ViewModel
        viewModel.fetchTrailer(movieId) { videoId ->
            if (videoId != null) {
                binding.youtubePlayerView.visibility = View.VISIBLE
                loadYouTubeVideo(videoId)
            } else {
                binding.youtubePlayerView.visibility = View.GONE
                Toast.makeText(requireContext(),
                    getString(R.string.trailer_not_available), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(movie: Movie) {
        binding.titleTextView.text = movie.title
        binding.overviewTextView.text = movie.overview
        binding.releaseDateTextView.text = movie.releaseDate
        binding.ratingTextView.text = getString(
            R.string.rating_with_star,
            getString(R.string.rating_format, movie.voteAverage)
        )

        // Load poster image
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/original${movie.posterPath}")
            .into(binding.backdropImageView)
    }

    private fun loadYouTubeVideo(videoId: String) {
        val youTubePlayerView: YouTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }

}
