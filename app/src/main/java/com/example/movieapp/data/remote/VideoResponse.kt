package com.example.movieapp.data.remote

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("results") val videos: List<Video>
)
data class Video(
    @SerializedName("key") val key: String, // ✅ YouTube video ID
    @SerializedName("site") val site: String, // Should be "YouTube"
    @SerializedName("type") val type: String // Should be "Trailer"
)
