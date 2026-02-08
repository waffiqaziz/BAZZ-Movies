package com.waffiq.bazz_movies.core.movie.di

import com.waffiq.bazz_movies.core.movie.data.repository.MoviesRepositoryImpl
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
fun interface MovieRepositoryModule {

  @Binds
  fun bindMovieRepository(moviesRepository: MoviesRepositoryImpl): IMoviesRepository
}
