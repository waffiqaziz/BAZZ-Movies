package com.waffiq.bazz_movies.core.di

import com.waffiq.bazz_movies.core.data.repository.MoviesRepository
import com.waffiq.bazz_movies.core.domain.repository.IMoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class MovieRepositoryModule {

  @Binds
  abstract fun bindMovieRepository(
    moviesRepository: MoviesRepository
  ): IMoviesRepository
}
