package com.waffiq.bazz_movies.feature.watchlist.di

import com.waffiq.bazz_movies.feature.watchlist.data.repository.WatchlistRepositoryImpl
import com.waffiq.bazz_movies.feature.watchlist.domain.repository.IWatchlistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
fun interface WatchlistRepositoryModule {

  @Binds
  fun bindWatchlistRepository(watchlistRepository: WatchlistRepositoryImpl): IWatchlistRepository
}
