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
        { localDataSource.getPopularMovies() },
        { remoteDataSource.getPopularMovies() },
        { movies ->
            val updatedMovies = movies.movies.map { newMovie ->
                val existingMovie = localDataSource.getMovieById(newMovie.id) // ✅ בדיקה אם קיים

                newMovie.copy(
                    isPopular = true,
                    isUpcoming = existingMovie?.isUpcoming ?: false, // ✅ שמירת `isUpcoming`
                    favorite = existingMovie?.favorite ?: false // ✅ שמירת `favorite`
                )
            }
            localDataSource.insertMovies(updatedMovies)
        }
    )


    fun getUpcomingMovies(): LiveData<Resource<List<Movie>>> = performFetchingAndSaving(
        { localDataSource.getUpcomingMovies() },
        { remoteDataSource.getUpcomingMovies() },
        { movies ->
            val updatedMovies = movies.movies.map { newMovie ->
                val existingMovie = localDataSource.getMovieById(newMovie.id) // ✅ בדיקה אם קיים

                newMovie.copy(
                    isPopular = existingMovie?.isPopular ?: false, // ✅ שמירת `isPopular`
                    isUpcoming = true,
                    favorite = existingMovie?.favorite ?: false // ✅ שמירת `favorite`
                )
            }
            localDataSource.insertMovies(updatedMovies)
        }
    )

    fun getMovieById(movieId: Int): LiveData<Resource<Movie>> = performFetchingAndSaving(
        { localDataSource.getMovieByIdLiveData(movieId) }, // ✅ Load from Room first
        { remoteDataSource.getMovieById(movieId) }, // ✅ Then fetch from API
        { movie ->
            val existingMovie = localDataSource.getMovieById(movie.id) // ✅ Preserve existing data
            val updatedMovie = movie.copy(
                isPopular = existingMovie?.isPopular ?: false,
                isUpcoming = existingMovie?.isUpcoming ?: false,
                favorite = existingMovie?.favorite ?: false
            )
            localDataSource.updateMovie(updatedMovie) // ✅ Save the updated movie
        }
    )


    suspend fun fetchPopularMovies() {
        val result = remoteDataSource.getPopularMovies()
        if (result is Resource.Success) {
            val updatedMovies = result.data!!.movies.map { movie ->
                val existingMovie = localDataSource.getMovieById(movie.id)
                movie.copy(
                    isPopular = true,
                    isUpcoming = existingMovie?.isUpcoming ?: false, // ✅ Preserve `isUpcoming`
                    favorite = existingMovie?.favorite ?: false // ✅ Preserve `favorite`
                )
            }
            localDataSource.insertMovies(updatedMovies)
        }
    }




    suspend fun fetchUpcomingMovies() {
        val result = remoteDataSource.getUpcomingMovies()
        if (result is Resource.Success) {
            val updatedMovies = result.data!!.movies.map { movie ->
                val existingMovie = localDataSource.getMovieById(movie.id)
                movie.copy(
                    isPopular = existingMovie?.isPopular ?: false, // ✅ Preserve `isPopular`
                    isUpcoming = true,
                    favorite = existingMovie?.favorite ?: false // ✅ Preserve `favorite`
                )
            }
            localDataSource.insertMovies(updatedMovies)
        }
    }



    fun getFavoriteMovies(): LiveData<List<Movie>> = localDataSource.getFavoriteMovies()

    suspend fun updateFavorite(movie: Movie) {
        val existingMovie = localDataSource.getMovieById(movie.id) // ✅ Fetch the current movie from DB
        if (existingMovie != null) {
            localDataSource.updateMovie(existingMovie.copy(favorite = !existingMovie.favorite)) // ✅ Only toggle favorite
        }
    }


}
