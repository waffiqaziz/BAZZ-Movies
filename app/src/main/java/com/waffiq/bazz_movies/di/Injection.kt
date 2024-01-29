package com.waffiq.bazz_movies.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.waffiq.bazz_movies.data.local.LocalDataSource
import com.waffiq.bazz_movies.data.local.model.UserPreference
import com.waffiq.bazz_movies.data.local.room.FavoriteDatabase
import com.waffiq.bazz_movies.data.remote.retrofit.CountryIPApiConfig
import com.waffiq.bazz_movies.data.remote.retrofit.IMDBApiLibConfig
import com.waffiq.bazz_movies.data.remote.retrofit.TMDBApiConfig
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.data.repository.UserRepository
import com.waffiq.bazz_movies.utils.AppExecutors

object Injection {
  fun provideMovieRepository(context: Context): MoviesRepository {
    val database = FavoriteDatabase.getInstance(context)
    val tmdbApiService = TMDBApiConfig.getApiService()
    val imdbApiLibService = IMDBApiLibConfig.getIMDBLibApiService()
    val localDataSource = LocalDataSource.getInstance(database.favoriteDao())
    val appExecutors = AppExecutors()

    return MoviesRepository(tmdbApiService, imdbApiLibService, localDataSource, appExecutors)
  }

  fun provideUserRepository(dataStore: DataStore<Preferences>): UserRepository {
    val tmdbApiService = TMDBApiConfig.getApiService()
    val countryIPApiService = CountryIPApiConfig.getApiService()
    val pref = UserPreference.getInstance(dataStore)

    return UserRepository(countryIPApiService, tmdbApiService, pref)
  }
}