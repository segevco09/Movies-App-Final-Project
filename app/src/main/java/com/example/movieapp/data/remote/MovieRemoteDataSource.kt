package com.example.movieapp.data.remote

import javax.inject.Inject


class MovieRemoteDataSource @Inject constructor(
    private val movieApiService: MovieApiService
) : BaseDataSource() {

    suspend fun getPopularMovies() = getResult { movieApiService.getPopularMovies() }
    suspend fun getUpcomingMovies() = getResult { movieApiService.getUpcomingMovies() }
    suspend fun getMovieById(movieId: Int) = getResult { movieApiService.getMovieById(movieId)}
    suspend fun getMovieVideos(movieId: Int) = getResult { movieApiService.getMovieVideos(movieId) }


}




