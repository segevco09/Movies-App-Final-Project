package com.example.movieapp.data

import androidx.lifecycle.LiveData
import com.example.movieapp.data.local.Movie
import com.example.movieapp.data.local.MovieDao
import com.example.movieapp.data.remote.MovieRemoteDataSource
import com.example.movieapp.utils.Resource
import com.example.movieapp.utils.performFetchingAndSaving
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource,
    private val localDataSource: MovieDao
) {

    fun getPopularMovies(): LiveData<Resource<List<Movie>>> = performFetchingAndSaving(
        { localDataSource.getAllMovies() },  // ✅ Fetch all movies
        { remoteDataSource.getPopularMovies() },  // ✅ Fetch from API if needed
        { movies ->
            movies.movies.forEach { it.isUpcoming = false } // ✅ Ensure these are NOT upcoming
            localDataSource.insertMovies(movies.movies)
        }
    )

    fun getUpcomingMovies(): LiveData<Resource<List<Movie>>> = performFetchingAndSaving(
        { localDataSource.getUpcomingMovies() }, // ✅ Fetch from local DB first
        { remoteDataSource.getUpcomingMovies() }, // ✅ Fetch from API only if needed
        { movies ->
            movies.movies.forEach { it.isUpcoming = true } // ✅ Mark these as upcoming
            localDataSource.insertMovies(movies.movies)
        }
    )

    suspend fun fetchPopularMovies() {
        val result = remoteDataSource.getPopularMovies()
        if (result is Resource.Success) {
            result.data!!.movies.forEach { it.isUpcoming = false } // ✅ Ensure these are NOT upcoming
            localDataSource.insertMovies(result.data!!.movies)
        }
    }

    suspend fun fetchUpcomingMovies() {
        val result = remoteDataSource.getUpcomingMovies()
        if (result is Resource.Success) {
            result.data!!.movies.forEach { it.isUpcoming = true } // ✅ Mark as upcoming
            localDataSource.insertMovies(result.data!!.movies)
        }
    }

    fun getFavoriteMovies(): LiveData<List<Movie>> = localDataSource.getFavoriteMovies()

    suspend fun updateFavorite(movie: Movie) {
        localDataSource.updateMovie(movie)
    }
}
