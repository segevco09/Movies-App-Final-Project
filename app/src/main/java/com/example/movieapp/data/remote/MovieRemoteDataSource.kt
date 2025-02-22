package com.example.movieapp.data.remote

import javax.inject.Inject
import com.example.movieapp.utils.LanguageUtils


class MovieRemoteDataSource @Inject constructor(
    private val movieApiService: MovieApiService
) : BaseDataSource() {
    private val language = LanguageUtils.getDeviceLanguage()

    suspend fun getPopularMovies() = getResult { movieApiService.getPopularMovies(language = language) }
    suspend fun getUpcomingMovies() = getResult { movieApiService.getUpcomingMovies(language = language) }
    suspend fun getMovieById(movieId: Int) = getResult { movieApiService.getMovieById(movieId, language = language) }
    suspend fun getMovieVideos(movieId: Int) = getResult { movieApiService.getMovieVideos(movieId, language = language) }


}




