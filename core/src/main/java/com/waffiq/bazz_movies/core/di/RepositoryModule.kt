package com.waffiq.bazz_movies.core.di

import com.waffiq.bazz_movies.core.data.repository.MoviesRepository
import com.waffiq.bazz_movies.core.data.repository.UserRepository
import com.waffiq.bazz_movies.core.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

  @Binds
  abstract fun bindMovieRepository(
    moviesRepository: MoviesRepository
  ): IMoviesRepository

  @Binds
  abstract fun bindUserRepository(
    userRepository: UserRepository
  ): IUserRepository
}
