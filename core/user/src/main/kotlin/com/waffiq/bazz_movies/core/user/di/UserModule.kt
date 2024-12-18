package com.waffiq.bazz_movies.core.user.di

import com.waffiq.bazz_movies.core.user.domain.usecase.getregion.GetRegionInteractor
import com.waffiq.bazz_movies.core.user.domain.usecase.getregion.GetRegionUseCase
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefInteractor
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class UserModule {

  @Binds
  @ViewModelScoped
  abstract fun bindUserPrefUseCase(
    userPrefInteractor: UserPrefInteractor
  ): UserPrefUseCase

  @Binds
  @ViewModelScoped
  abstract fun bindGetRegionUseCase(
    getRegionInteractor: GetRegionInteractor
  ): GetRegionUseCase
}
