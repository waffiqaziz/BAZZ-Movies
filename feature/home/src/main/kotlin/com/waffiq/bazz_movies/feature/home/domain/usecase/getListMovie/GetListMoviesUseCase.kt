package com.waffiq.bazz_movies.feature.home.domain.usecase.getListMovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.data.ResultItem
import kotlinx.coroutines.flow.Flow

interface GetListMoviesUseCase {
  fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>>
  fun getPagingPopularMovies(): Flow<PagingData<ResultItem>>
  fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItem>>
  fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItem>>
  fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItem>>
  fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItem>>
}
