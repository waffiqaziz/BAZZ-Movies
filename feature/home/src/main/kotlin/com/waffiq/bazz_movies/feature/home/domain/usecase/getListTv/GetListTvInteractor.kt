package com.waffiq.bazz_movies.feature.home.domain.usecase.getListTv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.data.ResultItem
import com.waffiq.bazz_movies.feature.home.domain.repository.IHomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListTvInteractor @Inject constructor(
  private val homeRepository: IHomeRepository
) : GetListTvUseCase {
  override fun getPagingPopularTv(region: String): Flow<PagingData<ResultItem>> =
    homeRepository.getPagingPopularTv(region)

  override fun getPagingAiringThisWeekTv(region: String): Flow<PagingData<ResultItem>> =
    homeRepository.getPagingAiringThisWeekTv(region)

  override fun getPagingAiringTodayTv(region: String): Flow<PagingData<ResultItem>> =
    homeRepository.getPagingAiringTodayTv(region)

  override fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>> =
    homeRepository.getPagingTopRatedTv()
}
