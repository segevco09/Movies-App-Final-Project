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

    @Query("SELECT * FROM movies WHERE favorite = 1")
    fun getFavoriteMovies(): LiveData<List<Movie>> // ✅ Correctly fetch favorite movies

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Update
    suspend fun updateMovie(movie : Movie)

    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :movieId AND favorite = 1)")
    suspend fun isMovieFavorite(movieId: Int): Boolean

    @Query("SELECT * FROM movies")
    suspend fun getAllMoviesList(): List<Movie> // ✅ Fetch movies synchronously


}
