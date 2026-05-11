package com.waffiq.bazz_movies.core.data.di.usecase

import com.waffiq.bazz_movies.core.data.domain.usecase.asian.GetAsianMediaInteractor
import com.waffiq.bazz_movies.core.data.domain.usecase.asian.GetAsianMediaUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
fun interface DiscoverUseCaseModule {

  @Binds
  @ViewModelScoped
  fun bindDiscoverUseCase(discoverInteractor: GetAsianMediaInteractor): GetAsianMediaUseCase
}
