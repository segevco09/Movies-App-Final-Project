package com.example.movieapp.di

import android.content.Context
import com.example.movieapp.MovieApp
import com.example.movieapp.data.MovieRepository
import com.example.movieapp.data.local.MovieDao
import com.example.movieapp.data.local.MovieDatabase
import com.example.movieapp.data.remote.MovieApiService
import com.example.movieapp.data.remote.MovieRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideApiService(): MovieApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MovieDatabase {
        return MovieDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }

    @Singleton
    @Provides
    fun provideMovieRemoteDataSource(
        movieApiService: MovieApiService
    ): MovieRemoteDataSource {
        return MovieRemoteDataSource(movieApiService)
    }

    @Singleton
    @Provides
    fun provideMovieRepository(
        remoteDataSource: MovieRemoteDataSource,
        movieDao: MovieDao
    ): MovieRepository {
        return MovieRepository(remoteDataSource, movieDao)
    }
}
