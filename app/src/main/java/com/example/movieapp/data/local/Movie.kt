package com.example.movieapp.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date


@Entity(tableName = "movies")
@Parcelize
data class Movie(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val release_date: String,
    val original_title: String,
    val backdrop_path: String,
    val original_language: String,
    val adult: Boolean,
    val video: Boolean,
    val popularity: Double,
    val vote_average: Double,
    val vote_count: Int,
    val poster_path: String,
    var favorite: Boolean = false,
    var isUpcoming: Boolean = false,
    var isPopular: Boolean = false
) : Parcelable
