package com.example.movieapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.MovieRepository
import com.example.movieapp.data.local.Movie
import com.example.movieapp.data.local.MovieDao
import com.example.movieapp.utils.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    val movies: LiveData<Resource<List<Movie>>> = repository.getPopularMovies()
    val upcomingMovies: LiveData<Resource<List<Movie>>> = repository.getUpcomingMovies()
    val favoriteMovies: LiveData<List<Movie>> = repository.getFavoriteMovies()

//    fun fetchPopularMovies() = viewModelScope.launch {
//        repository.fetchPopularMovies()
//    }

//    fun fetchUpcomingMovies() = viewModelScope.launch {
//        repository.fetchUpcomingMovies()
//    }

    fun updateFavorite(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        val updatedMovie = movie.copy(favorite = !movie.favorite) // ✅ Toggle favorite status
        repository.updateFavorite(updatedMovie)
    }
}
