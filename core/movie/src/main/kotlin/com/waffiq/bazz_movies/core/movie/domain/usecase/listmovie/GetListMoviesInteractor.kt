package com.waffiq.bazz_movies.core.movie.domain.usecase.listmovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetListMoviesInteractor @Inject constructor(
  private val movieRepository: IMoviesRepository,
  private val userRepository: IUserRepository,
) : GetListMoviesUseCase {

  private fun getRegion() =
    userRepository.getUserRegionPref()
      .filter { it.isNotEmpty() && it != NAN }
      .distinctUntilChanged()

  override fun getTopRatedMovies(): Flow<PagingData<MediaItem>> =
    movieRepository.getTopRatedMovies()

  override fun getPopularMovies(): Flow<PagingData<MediaItem>> = movieRepository.getPopularMovies()

  override fun getTrendingThisWeek(): Flow<PagingData<MediaItem>> =
    getRegion().flatMapLatest { region ->
      movieRepository.getTrendingThisWeek(region)
    }

  override fun getTrendingToday(): Flow<PagingData<MediaItem>> =
    getRegion().flatMapLatest { region ->
      movieRepository.getTrendingToday(region)
    }

  override fun getUpcomingMovies(): Flow<PagingData<MediaItem>> =
    getRegion().flatMapLatest { region ->
      movieRepository.getUpcomingMovies(region)
    }

  override fun getPlayingNowMovies(): Flow<PagingData<MediaItem>> =
    getRegion().flatMapLatest { region ->
      movieRepository.getPlayingNowMovies(region)
    }
}
