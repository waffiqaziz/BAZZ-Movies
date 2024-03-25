package com.waffiq.bazz_movies.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSource
import com.waffiq.bazz_movies.data.local.model.UserPreference
import com.waffiq.bazz_movies.data.local.room.FavoriteDatabase
import com.waffiq.bazz_movies.data.remote.datasource.RemoteDataSource
import com.waffiq.bazz_movies.data.remote.retrofit.OMDbApiConfig
import com.waffiq.bazz_movies.data.remote.retrofit.TMDBApiConfig
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.data.repository.UserRepository

object Injection {
  fun provideMovieRepository(context: Context): MoviesRepository {
    val database = FavoriteDatabase.getInstance(context)
    val remoteDataSource =
      RemoteDataSource.getInstance(
        TMDBApiConfig.getApiService(),
        OMDbApiConfig.getOMDBApiService()
      )
    val localDataSource = LocalDataSource.getInstance(database.favoriteDao())

    return MoviesRepository(localDataSource, remoteDataSource)
  }

  fun provideUserRepository(dataStore: DataStore<Preferences>): UserRepository {
    val pref = UserPreference.getInstance(dataStore)

    return UserRepository(pref)
  }
}