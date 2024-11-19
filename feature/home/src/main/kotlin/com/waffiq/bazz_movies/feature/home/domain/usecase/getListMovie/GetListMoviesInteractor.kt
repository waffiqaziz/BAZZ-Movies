package com.waffiq.bazz_movies.feature.home.domain.usecase.getListMovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.data.ResultItem
import com.waffiq.bazz_movies.feature.home.domain.repository.IHomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListMoviesInteractor @Inject constructor(
  private val homeRepository: IHomeRepository
) : GetListMoviesUseCase {
  override fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>> =
    homeRepository.getPagingTopRatedMovies()

  override fun getPagingPopularMovies(): Flow<PagingData<ResultItem>> =
    homeRepository.getPagingPopularMovies()

  override fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItem>> =
    homeRepository.getPagingTrendingWeek(region)

  override fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItem>> =
    homeRepository.getPagingTrendingDay(region)

  override fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItem>> =
    homeRepository.getPagingUpcomingMovies(region)

  override fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItem>> =
    homeRepository.getPagingPlayingNowMovies(region)
}
