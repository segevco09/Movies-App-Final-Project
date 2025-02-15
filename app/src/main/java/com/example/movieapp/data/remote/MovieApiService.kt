package com.example.movieapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = "ab31dd0cb696f61108161a49f49d3c02"
    ): MovieResponse
}


