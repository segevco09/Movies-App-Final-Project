package com.example.movieapp.data.remote

import com.example.movieapp.BuildConfig
import com.example.movieapp.data.local.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET("popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String =  BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<MovieResponse>

    @GET("{movie_id}")
    suspend fun getMovieById(
        @retrofit2.http.Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String =  BuildConfig.TMDB_API_KEY
    ): Response<Movie>

}
