package com.example.movieapp.data

import android.util.Log
import androidx.lifecycle.LiveData
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
    val movies: LiveData<Resource<List<Movie>>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = apiService.getAllMovies()
            if (response.movies.isNotEmpty()) {
                movieDao.insertMovies(response.movies.map { it.copy(favorite = movieDao.isMovieFavorite(it.id)) }) // ✅ Preserve favorite status
                emit(Resource.success(movieDao.getAllMoviesList()))
                Log.d("API_RESPONSE", "Movies received: ${response.movies.size}")
            } else {
                emit(Resource.error("No movies found"))
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error fetching movies: ${e.message}")
            emit(Resource.error("Failed to load movies. Check internet connection."))
        }
    }

    fun getFavoriteMovies(): LiveData<List<Movie>> = movieDao.getFavoriteMovies()

    suspend fun updateFavorite(movie: Movie) {
        movieDao.updateMovie(movie) // ✅ Ensure full update in Room
    }

    suspend fun getAllMoviesFromDB(): List<Movie> {
        return movieDao.getAllMoviesList()
    }

    suspend fun isMovieFavorite(movieId: Int): Boolean {
        return movieDao.isMovieFavorite(movieId)
    }
}