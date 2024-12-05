package com.waffiq.bazz_movies.feature.home.domain.usecase.getListTv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.data.ResultItem
import kotlinx.coroutines.flow.Flow

interface GetListTvUseCase {
  fun getPagingPopularTv(region: String): Flow<PagingData<ResultItem>>
  fun getPagingAiringThisWeekTv(region: String): Flow<PagingData<ResultItem>>
  fun getPagingAiringTodayTv(region: String): Flow<PagingData<ResultItem>>
  fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>>
}
