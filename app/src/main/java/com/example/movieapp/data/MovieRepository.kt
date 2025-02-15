package com.example.movieapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.movieapp.data.local.Movie
import com.example.movieapp.data.local.MovieDao
import com.example.movieapp.data.remote.MovieApiService
import com.example.movieapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val apiService: MovieApiService,
    private val movieDao: MovieDao
) {
    private val _movies = MutableLiveData<Resource<List<Movie>>>()
    val movies: LiveData<Resource<List<Movie>>> get() = _movies

    suspend fun refreshMovies() {
        val moviesFromDB = movieDao.getAllMoviesList()
        _movies.postValue(Resource.success(moviesFromDB))
    }

    fun getFavoriteMovies(): LiveData<List<Movie>> = movieDao.getFavoriteMovies()

    suspend fun updateFavorite(movie: Movie) {
        movieDao.updateMovie(movie) // ✅ Ensure full update in Room
        refreshMovies() // ✅ Ensure UI updates in all fragments
    }
}