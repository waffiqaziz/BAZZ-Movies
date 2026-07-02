package com.waffiq.bazz_movies.feature.detail.di

import com.waffiq.bazz_movies.feature.detail.domain.usecase.collection.GetMovieCollectionInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.collection.GetMovieCollectionUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
fun interface GetMovieCollectionUseCaseModule {

  @Binds
  @ViewModelScoped
  fun bindGetMovieCollectionUseCase(
    getMovieCollectionInteractor: GetMovieCollectionInteractor,
  ): GetMovieCollectionUseCase
}
