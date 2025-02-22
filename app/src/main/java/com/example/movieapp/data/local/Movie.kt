package com.example.movieapp.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("original_title")
    val originalTitle: String,

    @SerializedName("backdrop_path")
    val backdropPath: String,

    @SerializedName("original_language")
    val originalLanguage: String,

    val adult: Boolean,
    val video: Boolean,
    val popularity: Double,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Int,

    @SerializedName("poster_path")
    val posterPath: String,

    var favorite: Boolean = false,
    var isUpcoming: Boolean = false,
    var isPopular: Boolean = false
) : Parcelable
