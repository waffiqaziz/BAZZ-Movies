package com.waffiq.bazz_movies.core.data.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.data.domain.repository.ITrendingRepository
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.network.data.remote.datasource.trending.TrendingRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrendingRepositoryImpl @Inject constructor(
  private val trendingRemoteDataSource: TrendingRemoteDataSource,
) : ITrendingRepository {

  override fun getTrendingThisWeek(region: String): Flow<PagingData<MediaItem>> =
    trendingRemoteDataSource.getTrendingThisWeek(region).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }

  override fun getTrendingToday(region: String): Flow<PagingData<MediaItem>> =
    trendingRemoteDataSource.getTrendingToday(region).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }
}
