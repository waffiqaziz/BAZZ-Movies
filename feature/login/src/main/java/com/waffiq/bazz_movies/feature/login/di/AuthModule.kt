package com.waffiq.bazz_movies.feature.login.di

import com.waffiq.bazz_movies.core.user.domain.usecase.auth_tmdb_account.AuthTMDbAccountInteractor
import com.waffiq.bazz_movies.core.user.domain.usecase.auth_tmdb_account.AuthTMDbAccountUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthModule {

  // region USER
  @Binds
  @ViewModelScoped
  abstract fun bindAuthTMDbAccountUseCase(
    authTMDbAccountInteractor: AuthTMDbAccountInteractor
  ): AuthTMDbAccountUseCase
}
