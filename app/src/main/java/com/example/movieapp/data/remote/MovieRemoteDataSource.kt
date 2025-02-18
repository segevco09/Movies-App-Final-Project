package com.example.movieapp.data.remote

import javax.inject.Inject

import com.example.movieapp.data.remote.MovieApiService
import com.example.movieapp.utils.Resource
import retrofit2.Response

class MovieRemoteDataSource @Inject constructor(
    private val movieApiService: MovieApiService
) : BaseDataSource() {

    suspend fun getPopularMovies() = getResult { movieApiService.getPopularMovies("ab31dd0cb696f61108161a49f49d3c02") }
    suspend fun getUpcomingMovies() = getResult { movieApiService.getUpcomingMovies("ab31dd0cb696f61108161a49f49d3c02") }
    suspend fun getMovieById(movieId: Int) = getResult { movieApiService.getMovieById(movieId, "ab31dd0cb696f61108161a49f49d3c02")}

}




