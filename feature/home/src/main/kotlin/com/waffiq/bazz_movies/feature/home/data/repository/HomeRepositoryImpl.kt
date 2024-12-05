package com.waffiq.bazz_movies.feature.home.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.data.ResultItem
import com.waffiq.bazz_movies.core.movie.utils.mappers.ResultItemResponseMapper.toResultItem
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.feature.home.domain.repository.IHomeRepository
import com.waffiq.bazz_movies.feature.home.utils.helpers.Helper.getDateToday
import com.waffiq.bazz_movies.feature.home.utils.helpers.Helper.getDateTwoWeeksFromToday
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
  private val movieDataSource: MovieDataSource
) : IHomeRepository {

  override fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTrendingWeek(region).map { pagingData ->
      pagingData.map { it.toResultItem() }
    }

  override fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTrendingDay(region).map { pagingData ->
      pagingData.map { it.toResultItem() }
    }

  override fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTopRatedMovies().map { pagingData ->
      pagingData.map { it.toResultItem() }
    }

  override fun getPagingPopularMovies(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingPopularMovies().map { pagingData ->
      pagingData.map { it.toResultItem() }
    }

  override fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingUpcomingMovies(region).map { pagingData ->
      pagingData.map { it.toResultItem() }
    }

  override fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingPlayingNowMovies(region).map { pagingData ->
      pagingData.map { it.toResultItem() }
    }

  override fun getPagingAiringTodayTv(region: String): Flow<PagingData<ResultItem>> {
    val date = getDateToday()
    return movieDataSource.getPagingAiringTodayTv(region, date, date)
      .map { pagingData ->
        pagingData.map { it.toResultItem() }
      }
  }

  override fun getPagingPopularTv(region: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingPopularTv(region, getDateTwoWeeksFromToday()).map { pagingData ->
      pagingData.map { it.toResultItem() }
    }

  override fun getPagingOnTv(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingOnTv().map { pagingData ->
      pagingData.map { it.toResultItem() }
    }

  override fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTopRatedTv().map { pagingData ->
      pagingData.map { it.toResultItem() }
    }
}
