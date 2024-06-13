package com.waffiq.bazz_movies.di


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.waffiq.bazz_movies.data.local.model.UserPreference
import com.waffiq.bazz_movies.data.remote.datasource.UserDataSource
import com.waffiq.bazz_movies.data.remote.retrofit.CountryIPApiConfig
import com.waffiq.bazz_movies.data.remote.retrofit.TMDBApiConfig
import com.waffiq.bazz_movies.data.repository.UserRepository
import com.waffiq.bazz_movies.domain.usecase.auth_tmdb_account.AuthTMDbAccountInteractor
import com.waffiq.bazz_movies.domain.usecase.auth_tmdb_account.AuthTMDbAccountUseCase
import com.waffiq.bazz_movies.domain.usecase.get_region.GetRegionInteractor
import com.waffiq.bazz_movies.domain.usecase.get_region.GetRegionUseCase
import com.waffiq.bazz_movies.domain.usecase.user_pref.UserPrefInteractor
import com.waffiq.bazz_movies.domain.usecase.user_pref.UserPrefUseCase

object UserUseCaseProvider {
  fun provideUserRepository(dataStore: DataStore<Preferences>): UserRepository {
    val pref = UserPreference.getInstance(dataStore)
    val userDataSource =
      UserDataSource.getInstance(
        TMDBApiConfig.getApiService(),
        CountryIPApiConfig.getApiService()
      )
    return UserRepository(pref, userDataSource)
  }

  // region USER
  fun provideAuthTMDbAccountUseCase(dataStore: DataStore<Preferences>): AuthTMDbAccountUseCase {
    val repository = provideUserRepository(dataStore)
    return AuthTMDbAccountInteractor(repository)
  }

  fun provideUserPrefUseCase(dataStore: DataStore<Preferences>): UserPrefUseCase {
    val repository = provideUserRepository(dataStore)
    return UserPrefInteractor(repository)
  }

  fun provideGetRegion(dataStore: DataStore<Preferences>): GetRegionUseCase{
    val repository = provideUserRepository(dataStore)
    return GetRegionInteractor(repository)
  }
  // endregion USER
}