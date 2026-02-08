package com.waffiq.bazz_movies.feature.home.domain.usecase.getListMovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.home.domain.repository.IHomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListMoviesInteractor @Inject constructor(private val homeRepository: IHomeRepository) :
  GetListMoviesUseCase {
  override fun getTopRatedMovies(): Flow<PagingData<MediaItem>> = homeRepository.getTopRatedMovies()

  override fun getPopularMovies(): Flow<PagingData<MediaItem>> = homeRepository.getPopularMovies()

  override fun getTrendingThisWeek(region: String): Flow<PagingData<MediaItem>> =
    homeRepository.getTrendingThisWeek(region)

  override fun getTrendingToday(region: String): Flow<PagingData<MediaItem>> =
    homeRepository.getTrendingToday(region)

  override fun getUpcomingMovies(region: String): Flow<PagingData<MediaItem>> =
    homeRepository.getUpcomingMovies(region)

  override fun getPlayingNowMovies(region: String): Flow<PagingData<MediaItem>> =
    homeRepository.getPlayingNowMovies(region)
}
