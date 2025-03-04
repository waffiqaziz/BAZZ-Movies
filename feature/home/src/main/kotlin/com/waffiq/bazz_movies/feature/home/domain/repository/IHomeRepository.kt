package com.waffiq.bazz_movies.feature.home.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.ResultItem
import kotlinx.coroutines.flow.Flow

interface IHomeRepository {
  fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItem>>
  fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>>
  fun getPagingPopularMovies(): Flow<PagingData<ResultItem>>
  fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItem>>
  fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItem>>
  fun getPagingPopularTv(region: String): Flow<PagingData<ResultItem>>
  fun getPagingAiringThisWeekTv(region: String): Flow<PagingData<ResultItem>>
  fun getPagingAiringTodayTv(region: String): Flow<PagingData<ResultItem>>
  fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItem>>
  fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>>
}
