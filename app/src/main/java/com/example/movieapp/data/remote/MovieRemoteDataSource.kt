package com.example.movieapp.data.remote

import android.util.Log
import com.example.movieapp.BuildConfig
import javax.inject.Inject

import com.example.movieapp.data.remote.MovieApiService
import com.example.movieapp.utils.Resource
import retrofit2.Response

class MovieRemoteDataSource @Inject constructor(
    private val movieApiService: MovieApiService
) : BaseDataSource() {

    suspend fun getPopularMovies() = getResult { movieApiService.getPopularMovies() }
    suspend fun getUpcomingMovies() = getResult { movieApiService.getUpcomingMovies() }
    suspend fun getMovieById(movieId: Int) = getResult { movieApiService.getMovieById(movieId)}

}




