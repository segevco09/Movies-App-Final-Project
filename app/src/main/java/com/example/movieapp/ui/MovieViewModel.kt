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
    private val _movies = MutableLiveData<Resource<List<Movie>>>()
    val movies: LiveData<Resource<List<Movie>>> get() = _movies

    val favoriteMovies: LiveData<List<Movie>> = repository.getFavoriteMovies()

    init {
        fetchMovies()
    }

    private fun fetchMovies() = viewModelScope.launch {
        repository.movies.observeForever { _movies.postValue(it) }
    }

    fun updateFavorite(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        val updatedMovie = movie.copy(favorite = !movie.favorite) // Toggle favorite status
        repository.updateFavorite(updatedMovie)
        val updatedMovies = repository.getAllMoviesFromDB()
        withContext(Dispatchers.Main) {
            _movies.postValue(Resource.success(updatedMovies))
        }
    }
}


//    fun updateFavorite(movieId: Int, isFavorite: Boolean) = viewModelScope.launch(Dispatchers.IO) {
//        repository.updateFavorite(movieId, isFavorite)
//        val updatedMovies = repository.getAllMoviesFromDB().map {
//            it.copy(favorite = repository.isMovieFavorite(it.id)) // ✅ Ensure updated favorite status
//        }
//        _movies.postValue(Resource.success(updatedMovies))
//    }


