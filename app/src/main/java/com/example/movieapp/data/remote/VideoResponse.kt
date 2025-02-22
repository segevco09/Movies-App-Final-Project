package com.example.movieapp.data.remote

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("results") val videos: List<Video>
)
data class Video(
    val key: String, // YouTube video ID
    val site: String, // Should be "YouTube"
    val type: String // Should be "Trailer"
)
