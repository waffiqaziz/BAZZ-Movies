package com.waffiq.bazz_movies.core.di

import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.data.repository.UserRepository
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class, DatabaseModule::class, DatastoreModule::class])
abstract class RepositoryModule {

  @Binds
  abstract fun provideMovieRepository(moviesRepository: MoviesRepository): IMoviesRepository

  @Binds
  abstract fun provideUserRepository(userRepository: UserRepository): IUserRepository

}