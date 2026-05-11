package com.waffiq.bazz_movies.core.data.di.repository

import com.waffiq.bazz_movies.core.data.data.repository.MoviesRepositoryImpl
import com.waffiq.bazz_movies.core.data.domain.repository.IMoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
fun interface MovieRepositoryModule {

  @Binds
  fun bindMovieRepository(moviesRepositoryImpl: MoviesRepositoryImpl): IMoviesRepository
}
