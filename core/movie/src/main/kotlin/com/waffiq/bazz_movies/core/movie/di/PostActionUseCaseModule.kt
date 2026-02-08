package com.waffiq.bazz_movies.core.movie.di

import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.PostActionInteractor
import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.PostActionUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
fun interface PostActionUseCaseModule {

  @Binds
  @ViewModelScoped
  fun bindPostActionUseCase(postActionInteractor: PostActionInteractor): PostActionUseCase
}
