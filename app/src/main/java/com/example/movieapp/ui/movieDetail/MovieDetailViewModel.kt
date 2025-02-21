package com.example.movieapp.ui.movieDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.MovieRepository
import com.example.movieapp.data.local.Movie
import com.example.movieapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    fun getMovieById(movieId: Int): LiveData<Resource<Movie>> = 
        repository.getMovieById(movieId)

    fun fetchTrailer(movieId: Int, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            val videoId = repository.getMovieTrailer(movieId)
            onResult(videoId)
        }
    }
} 