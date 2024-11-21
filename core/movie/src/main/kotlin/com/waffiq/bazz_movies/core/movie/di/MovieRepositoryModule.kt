package com.waffiq.bazz_movies.core.movie.di

import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.movie.data.repository.MoviesRepository
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
