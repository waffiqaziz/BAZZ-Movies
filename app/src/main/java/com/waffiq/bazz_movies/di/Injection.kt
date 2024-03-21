package com.waffiq.bazz_movies.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSource
import com.waffiq.bazz_movies.data.local.model.UserPreference
import com.waffiq.bazz_movies.data.local.room.FavoriteDatabase
import com.waffiq.bazz_movies.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.data.remote.retrofit.TMDBApiConfig
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.data.repository.UserRepository

object Injection {
  fun provideMovieRepository(context: Context): MoviesRepository {
    val database = FavoriteDatabase.getInstance(context)
    val movieDataSource = MovieDataSource.getInstance(TMDBApiConfig.getApiService())
    val localDataSource = LocalDataSource.getInstance(database.favoriteDao())

    return MoviesRepository(localDataSource, movieDataSource)
  }

  fun provideUserRepository(dataStore: DataStore<Preferences>): UserRepository {
    val pref = UserPreference.getInstance(dataStore)

    return UserRepository(pref)
  }
}