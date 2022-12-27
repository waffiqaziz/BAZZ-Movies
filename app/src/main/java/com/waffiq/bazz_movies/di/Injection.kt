package com.waffiq.bazz_movies.di

import android.content.Context
import com.waffiq.bazz_movies.data.local.LocalDataSource
import com.waffiq.bazz_movies.data.local.room.FavoriteDatabase
import com.waffiq.bazz_movies.data.remote.retrofit.ApiConfig
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.utils.AppExecutors

object Injection {
  fun provideMovieRepository(context: Context): MoviesRepository {
    val database = FavoriteDatabase.getInstance(context)
    val apiService = ApiConfig.getApiService()
    val localDataSource = LocalDataSource.getInstance(database.favoriteDao())
    val appExecutors = AppExecutors()

    return MoviesRepository(apiService, localDataSource, appExecutors)
  }
}