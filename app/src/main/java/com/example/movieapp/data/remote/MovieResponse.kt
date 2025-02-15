package com.example.movieapp.data.remote

import com.example.movieapp.data.local.Movie
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results") val movies: List<Movie>
)