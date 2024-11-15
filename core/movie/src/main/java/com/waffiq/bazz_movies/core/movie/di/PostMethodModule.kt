package com.waffiq.bazz_movies.core.movie.di

import com.waffiq.bazz_movies.core.movie.domain.usecase.post_method.PostMethodInteractor
import com.waffiq.bazz_movies.core.movie.domain.usecase.post_method.PostMethodUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class PostMethodModule {

  @Binds
  @ViewModelScoped
  abstract fun bindPostMethodUseCase(
    postMethodInteractor: PostMethodInteractor
  ): PostMethodUseCase
}
