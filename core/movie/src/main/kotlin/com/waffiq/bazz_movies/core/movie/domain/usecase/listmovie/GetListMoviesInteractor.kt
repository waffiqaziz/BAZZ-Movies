package com.waffiq.bazz_movies.core.movie.domain.usecase.listmovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListMoviesInteractor @Inject constructor(private val movieRepository: IMoviesRepository) :
  GetListMoviesUseCase {

  override fun getTopRatedMovies(): Flow<PagingData<MediaItem>> =
    movieRepository.getTopRatedMovies()

  override fun getPopularMovies(): Flow<PagingData<MediaItem>> = movieRepository.getPopularMovies()

  override fun getTrendingThisWeek(region: String): Flow<PagingData<MediaItem>> =
    movieRepository.getTrendingThisWeek(region)

  override fun getTrendingToday(region: String): Flow<PagingData<MediaItem>> =
    movieRepository.getTrendingToday(region)

  override fun getUpcomingMovies(region: String): Flow<PagingData<MediaItem>> =
    movieRepository.getUpcomingMovies(region)

  override fun getPlayingNowMovies(region: String): Flow<PagingData<MediaItem>> =
    movieRepository.getPlayingNowMovies(region)
}
