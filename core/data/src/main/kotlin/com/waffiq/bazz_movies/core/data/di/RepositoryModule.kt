package com.waffiq.bazz_movies.core.data.di

import com.waffiq.bazz_movies.core.data.data.repository.AccountRepositoryImpl
import com.waffiq.bazz_movies.core.data.data.repository.MoviesRepositoryImpl
import com.waffiq.bazz_movies.core.data.data.repository.TrendingRepositoryImpl
import com.waffiq.bazz_movies.core.data.data.repository.TvRepositoryImpl
import com.waffiq.bazz_movies.core.data.domain.repository.IAccountRepository
import com.waffiq.bazz_movies.core.data.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.data.domain.repository.ITrendingRepository
import com.waffiq.bazz_movies.core.data.domain.repository.ITvRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

  @Binds
  fun bindAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): IAccountRepository

  @Binds
  fun bindMovieRepository(moviesRepositoryImpl: MoviesRepositoryImpl): IMoviesRepository

  @Binds
  fun bindTvRepository(trendingRepositoryImpl: TvRepositoryImpl): ITvRepository

  @Binds
  fun bindTrendingRepository(trendingRepositoryImpl: TrendingRepositoryImpl): ITrendingRepository
}
