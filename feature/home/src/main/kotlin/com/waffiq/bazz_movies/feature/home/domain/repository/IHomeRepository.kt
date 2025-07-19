package com.waffiq.bazz_movies.feature.home.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow

interface IHomeRepository {
  fun getTrendingToday(region: String): Flow<PagingData<MediaItem>>
  fun getTopRatedMovies(): Flow<PagingData<MediaItem>>
  fun getPopularMovies(): Flow<PagingData<MediaItem>>
  fun getUpcomingMovies(region: String): Flow<PagingData<MediaItem>>
  fun getPlayingNowMovies(region: String): Flow<PagingData<MediaItem>>
  fun getPopularTv(region: String): Flow<PagingData<MediaItem>>
  fun getAiringThisWeekTv(region: String): Flow<PagingData<MediaItem>>
  fun getAiringTodayTv(region: String): Flow<PagingData<MediaItem>>
  fun getTrendingThisWeek(region: String): Flow<PagingData<MediaItem>>
  fun getTopRatedTv(): Flow<PagingData<MediaItem>>
}
