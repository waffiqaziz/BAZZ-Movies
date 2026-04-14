package com.waffiq.bazz_movies.feature.watchlist.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.network.data.remote.datasource.account.AccountRemoteDataSource
import com.waffiq.bazz_movies.feature.watchlist.domain.repository.IWatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchlistRepositoryImpl @Inject constructor(
  private val accountRemoteDataSource: AccountRemoteDataSource,
) : IWatchlistRepository {

  override fun getWatchlistMovies(userId: Int, sessionId: String): Flow<PagingData<MediaItem>> =
    accountRemoteDataSource.getWatchlistMovies(userId, sessionId).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }

  override fun getWatchlistTv(userId: Int, sessionId: String): Flow<PagingData<MediaItem>> =
    accountRemoteDataSource.getWatchlistTv(userId, sessionId).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }
}
