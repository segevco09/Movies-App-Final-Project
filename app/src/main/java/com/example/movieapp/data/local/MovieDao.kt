package com.example.movieapp.data.local  // ✅ Ensure package is correct

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE isUpcoming = 1") // ✅ Fetch only upcoming movies
    fun getUpcomingMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE favorite = 1")
    fun getFavoriteMovies(): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Update
    suspend fun updateMovie(movie: Movie)
}


