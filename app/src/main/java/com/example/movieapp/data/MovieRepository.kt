package com.example.movieapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.movieapp.data.local.Movie
import com.example.movieapp.data.local.MovieDao
import com.example.movieapp.data.remote.MovieApiService
import com.example.movieapp.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val apiService: MovieApiService,
    private val movieDao: MovieDao
) {
    val movies: LiveData<Resource<List<Movie>>> = liveData {
        emit(Resource.loading())
        try {
            val response = apiService.getPopularMovies("ab31dd0cb696f61108161a49f49d3c02")
            Log.d("API_RESPONSE", "Movies received: ${response.movies.size}")
            movieDao.insertMovies(response.movies)
            emit(Resource.success(response.movies))
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error fetching movies: ${e.message}")
            emit(Resource.error(e.message ?: "Unknown Error"))
        }
    }
}
