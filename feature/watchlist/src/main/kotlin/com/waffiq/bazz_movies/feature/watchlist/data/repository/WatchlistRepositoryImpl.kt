package com.waffiq.bazz_movies.feature.watchlist.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.feature.watchlist.domain.repository.IWatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchlistRepositoryImpl @Inject constructor(private val movieDataSource: MovieDataSource) :
  IWatchlistRepository {

  override fun getWatchlistMovies(sessionId: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getWatchlistMovies(sessionId).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }

  override fun getWatchlistTv(sessionId: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getWatchlistTv(sessionId).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }
}
