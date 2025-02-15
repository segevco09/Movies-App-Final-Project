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
            val response = apiService.getPopularMovies()  // ✅ No need to pass API key manually
            if (response.movies.isNotEmpty()) {
                movieDao.insertMovies(response.movies)
                emit(Resource.success(response.movies))
                Log.d("API_RESPONSE", "Movies received: ${response.movies.size}")
            } else {
                emit(Resource.error("No movies found"))
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error fetching movies: ${e.message}")
            emit(Resource.error("Failed to load movies. Check internet connection."))
        }

    }

    fun getFavoriteMovies(): LiveData<List<Movie>> =
        movieDao.getFavoriteMovies() // ✅ Fetch favorite movies

    suspend fun updateFavorite(movieId: Int, isFavorite: Boolean) {
        movieDao.updateFavorite(movieId, isFavorite) // ✅ Update favorite status
    }
}
