package com.waffiq.bazz_movies.core.movie.domain.usecase.listtv

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

class GetListTvInteractor @Inject constructor(
  private val movieRepository: IMoviesRepository,
  private val userRepository: IUserRepository,
) : GetListTvUseCase {

  private fun getRegion() =
    userRepository.getUserRegionPref()
      .filter { it.isNotEmpty() && it != NAN }
      .distinctUntilChanged()

  override fun getPopularTv(): Flow<PagingData<MediaItem>> =
    getRegion().flatMapLatest { region ->
      movieRepository.getPopularTv(region)
    }

  override fun getAiringThisWeekTv(): Flow<PagingData<MediaItem>> =
    getRegion().flatMapLatest { region ->
      movieRepository.getAiringThisWeekTv(region)
    }

  override fun getAiringTodayTv(): Flow<PagingData<MediaItem>> =
    getRegion().flatMapLatest { region ->
      movieRepository.getAiringTodayTv(region)
    }

  override fun getTopRatedTv(): Flow<PagingData<MediaItem>> = movieRepository.getTopRatedTv()

  override fun getTvRecommendation(tvId: Int): Flow<PagingData<MediaItem>> =
    movieRepository.getTvRecommendation(tvId)
}
