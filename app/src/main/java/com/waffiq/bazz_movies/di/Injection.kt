package com.waffiq.bazz_movies.di

import android.content.Context
import com.waffiq.bazz_movies.data.remote.retrofit.ApiConfig
import com.waffiq.bazz_movies.data.room.MovieDatabase
import com.waffiq.bazz_movies.data.repository.MoviesRepository

object Injection {
  fun provideMovieRepository(context: Context): MoviesRepository {
    val database = MovieDatabase.getInstance(context)
    val apiService = ApiConfig.getApiService()
    return MoviesRepository(database, apiService)
  }
}