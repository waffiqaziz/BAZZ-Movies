package com.waffiq.bazz_movies.feature.home.domain.usecase.getListTv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.home.domain.repository.IHomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListTvInteractor @Inject constructor(private val homeRepository: IHomeRepository) :
  GetListTvUseCase {
  override fun getPopularTv(region: String): Flow<PagingData<MediaItem>> =
    homeRepository.getPopularTv(region)

  override fun getAiringThisWeekTv(region: String): Flow<PagingData<MediaItem>> =
    homeRepository.getAiringThisWeekTv(region)

  override fun getAiringTodayTv(region: String): Flow<PagingData<MediaItem>> =
    homeRepository.getAiringTodayTv(region)

  override fun getTopRatedTv(): Flow<PagingData<MediaItem>> = homeRepository.getTopRatedTv()
}
