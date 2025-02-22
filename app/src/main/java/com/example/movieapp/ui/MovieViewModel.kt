package com.example.movieapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.MovieRepository
import com.example.movieapp.data.local.Movie
import com.example.movieapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    val popularMovies: LiveData<Resource<List<Movie>>> = repository.getPopularMovies()
    val upcomingMovies: LiveData<Resource<List<Movie>>> = repository.getUpcomingMovies()
    val favoriteMovies: LiveData<List<Movie>> = repository.getFavoriteMovies()

    // For toggling the favorite status
    fun updateFavorite(movie: Movie) = viewModelScope.launch {
        val updatedMovie = movie.copy(favorite = !movie.favorite)
        repository.updateFavorite(updatedMovie)
    }

    // For editing movie details
    fun updateMovie(movie: Movie) = viewModelScope.launch {
        repository.updateMovie(movie)
    }

    fun fetchPopularMovies() = viewModelScope.launch {
        repository.refreshPopularMovies()
    }

    fun fetchUpcomingMovies() = viewModelScope.launch {
        repository.refreshUpcomingMovies()
    }
}

