package com.waffiq.bazz_movies.feature.list.di

import com.waffiq.bazz_movies.feature.list.domain.usecase.GetListInteractor
import com.waffiq.bazz_movies.feature.list.domain.usecase.GetListUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
interface GetListUseCaseModule {

  @Binds
  @ViewModelScoped
  fun bindList(getListInteractor: GetListInteractor): GetListUseCase
}
