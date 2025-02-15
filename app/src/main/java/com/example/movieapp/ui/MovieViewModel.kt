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

    val movies: LiveData<Resource<List<Movie>>> = repository.movies

    val favoriteMovies: LiveData<List<Movie>> = repository.getFavoriteMovies() // ✅ Get favorite movies

    fun updateFavorite(movieId: Int, isFavorite: Boolean) = viewModelScope.launch {
        repository.updateFavorite(movieId, isFavorite)
    }
}

