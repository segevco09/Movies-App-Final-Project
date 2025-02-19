package com.example.movieapp.data

import androidx.lifecycle.LiveData
import com.example.movieapp.BuildConfig
import com.example.movieapp.data.local.Movie
import com.example.movieapp.data.local.MovieDao
import com.example.movieapp.data.remote.MovieApiService
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
                val existingMovie = localDataSource.getMovieById(newMovie.id)

                if (existingMovie?.favorite == true) {
                    existingMovie // ✅ Keep local changes if the movie is a favorite
                } else {
                    newMovie.copy(
                        isPopular = true,
                        isUpcoming = existingMovie?.isUpcoming ?: false,
                        favorite = existingMovie?.favorite ?: false
                    )
                }
            }
            localDataSource.insertMovies(updatedMovies)
        }
    )

    fun getUpcomingMovies(): LiveData<Resource<List<Movie>>> = performFetchingAndSaving(
        { localDataSource.getUpcomingMovies() },
        { remoteDataSource.getUpcomingMovies() },
        { movies ->
            val updatedMovies = movies.movies.map { newMovie ->
                val existingMovie = localDataSource.getMovieById(newMovie.id)

                if (existingMovie?.favorite == true) {
                    existingMovie // ✅ Keep local changes if the movie is a favorite
                } else {
                    newMovie.copy(
                        isPopular = existingMovie?.isPopular ?: false,
                        isUpcoming = true,
                        favorite = existingMovie?.favorite ?: false
                    )
                }
            }
            localDataSource.insertMovies(updatedMovies)
        }
    )

    fun getMovieById(movieId: Int): LiveData<Resource<Movie>> = performFetchingAndSaving(
        { localDataSource.getMovieByIdLiveData(movieId) },
        { remoteDataSource.getMovieById(movieId) },
        { movie ->
            val existingMovie = localDataSource.getMovieById(movie.id)

            val updatedMovie = if (existingMovie?.favorite == true) {
                existingMovie // ✅ Keep local changes if the movie is a favorite
            } else {
                movie.copy(
                    isPopular = existingMovie?.isPopular ?: false,
                    isUpcoming = existingMovie?.isUpcoming ?: false,
                    favorite = existingMovie?.favorite ?: false
                )
            }
            localDataSource.updateMovie(updatedMovie)
        }
    )

    suspend fun fetchPopularMovies() {
        val result = remoteDataSource.getPopularMovies()
        if (result is Resource.Success) {
            val updatedMovies = result.data!!.movies.map { movie ->
                val existingMovie = localDataSource.getMovieById(movie.id)

                if (existingMovie?.favorite == true) {
                    existingMovie // ✅ Keep local changes if the movie is a favorite
                } else {
                    movie.copy(
                        isPopular = true,
                        isUpcoming = existingMovie?.isUpcoming ?: false,
                        favorite = existingMovie?.favorite ?: false
                    )
                }
            }
            localDataSource.insertMovies(updatedMovies)
        }
    }

    suspend fun fetchUpcomingMovies() {
        val result = remoteDataSource.getUpcomingMovies()
        if (result is Resource.Success) {
            val updatedMovies = result.data!!.movies.map { movie ->
                val existingMovie = localDataSource.getMovieById(movie.id)

                if (existingMovie?.favorite == true) {
                    existingMovie // ✅ Keep local changes if the movie is a favorite
                } else {
                    movie.copy(
                        isPopular = existingMovie?.isPopular ?: false,
                        isUpcoming = true,
                        favorite = existingMovie?.favorite ?: false
                    )
                }
            }
            localDataSource.insertMovies(updatedMovies)
        }
    }

    fun getFavoriteMovies(): LiveData<List<Movie>> = localDataSource.getFavoriteMovies()

    suspend fun updateFavorite(movie: Movie) {
        val existingMovie = localDataSource.getMovieById(movie.id)
        if (existingMovie != null) {
            localDataSource.updateMovie(existingMovie.copy(favorite = !existingMovie.favorite))
        }
    }

    suspend fun getMovieTrailer(movieId: Int): String? {
        return try {
            val result = remoteDataSource.getMovieVideos(movieId)
            if (result is Resource.Success) {
                result.data?.videos
                    ?.firstOrNull { it.site == "YouTube" && it.type == "Trailer" }
                    ?.key
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    suspend fun updateMovie(movie: Movie) {
        localDataSource.updateMovie(movie) // ✅ Update edited movies in the database
    }
}
