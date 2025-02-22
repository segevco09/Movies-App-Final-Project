package com.example.movieapp.data.remote

import com.example.movieapp.BuildConfig
import com.example.movieapp.data.local.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

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
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String =  BuildConfig.TMDB_API_KEY
    ): Response<Movie>

    @GET("{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): Response<VideoResponse>

}
