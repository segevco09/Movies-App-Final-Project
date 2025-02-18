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
    fun getMovieById(movieId: Int): LiveData<Resource<Movie>> = repository.getMovieById(movieId)


    // ✅ Fetch favorites directly from Room (No need for MutableLiveData)
    val favoriteMovies: LiveData<List<Movie>> = repository.getFavoriteMovies()

    fun updateFavorite(movie: Movie) = viewModelScope.launch {
        val updatedMovie = movie.copy(favorite = !movie.favorite) // ✅ Toggle favorite status
        repository.updateFavorite(updatedMovie)
    }
}
