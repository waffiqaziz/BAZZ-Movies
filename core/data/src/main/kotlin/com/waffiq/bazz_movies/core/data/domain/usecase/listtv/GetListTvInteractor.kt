package com.waffiq.bazz_movies.core.data.domain.usecase.listtv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.data.domain.repository.ITvRepository
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetListTvInteractor @Inject constructor(
  private val tvRepository: ITvRepository,
  private val userRepository: IUserRepository,
) : GetListTvUseCase {

  private fun getRegion() =
    userRepository.getUserRegionPref()
      .filter { it.isNotEmpty() && it != NAN }
      .distinctUntilChanged()

  override fun getPopularTv(): Flow<PagingData<MediaItem>> =
    getRegion().flatMapLatest { region ->
      tvRepository.getPopularTv(region)
    }

  override fun getAiringThisWeekTv(): Flow<PagingData<MediaItem>> =
    getRegion().flatMapLatest { region ->
      tvRepository.getAiringThisWeekTv(region)
    }

  override fun getAiringTodayTv(): Flow<PagingData<MediaItem>> =
    getRegion().flatMapLatest { region ->
      tvRepository.getAiringTodayTv(region)
    }

  override fun getTopRatedTv(): Flow<PagingData<MediaItem>> = tvRepository.getTopRatedTv()

  override fun getTvRecommendation(tvId: Int): Flow<PagingData<MediaItem>> =
    tvRepository.getTvRecommendation(tvId)
}
