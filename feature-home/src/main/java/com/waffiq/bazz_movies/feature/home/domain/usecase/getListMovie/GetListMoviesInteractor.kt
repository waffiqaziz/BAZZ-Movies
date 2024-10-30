package com.waffiq.bazz_movies.feature.home.domain.usecase.getListMovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListMoviesInteractor @Inject constructor(
  private val getListMoviesRepository: IMoviesRepository
) : GetListMoviesUseCase {
  override fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>> =
    getListMoviesRepository.getPagingTopRatedMovies()

  override fun getPagingPopularMovies(): Flow<PagingData<ResultItem>> =
    getListMoviesRepository.getPagingPopularMovies()

  override fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItem>> =
    getListMoviesRepository.getPagingTrendingWeek(region)

  override fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItem>> =
    getListMoviesRepository.getPagingTrendingDay(region)

  override fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItem>> =
    getListMoviesRepository.getPagingUpcomingMovies(region)

  override fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItem>> =
    getListMoviesRepository.getPagingPlayingNowMovies(region)
}
