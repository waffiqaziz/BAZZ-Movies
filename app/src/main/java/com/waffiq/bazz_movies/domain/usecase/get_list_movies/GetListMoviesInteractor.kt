package com.waffiq.bazz_movies.domain.usecase.get_list_movies

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow

class GetListMoviesInteractor(
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
