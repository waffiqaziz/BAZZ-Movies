package com.waffiq.bazz_movies.core.movie.di

import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.MediaStateInteractor
import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.MediaStateUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
interface MediaStateUseCaseModule {

  @Binds
  @ViewModelScoped
  fun bindMediaStateUseCase(mediaStateInteractor: MediaStateInteractor): MediaStateUseCase
}
