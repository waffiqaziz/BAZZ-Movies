package com.waffiq.bazz_movies.core.movie.di

import com.waffiq.bazz_movies.core.movie.domain.usecase.listmovie.GetListMoviesInteractor
import com.waffiq.bazz_movies.core.movie.domain.usecase.listmovie.GetListMoviesUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.listtv.GetListTvInteractor
import com.waffiq.bazz_movies.core.movie.domain.usecase.listtv.GetListTvUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
interface ListUseCaseModule {

  @Binds
  @ViewModelScoped
  fun bindListMoviesUseCase(getListMoviesInteractor: GetListMoviesInteractor): GetListMoviesUseCase

  @Binds
  @ViewModelScoped
  fun bindListTvUseCase(getListTvInteractor: GetListTvInteractor): GetListTvUseCase
}
