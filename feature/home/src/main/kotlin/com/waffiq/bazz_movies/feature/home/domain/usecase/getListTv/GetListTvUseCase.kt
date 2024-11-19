package com.waffiq.bazz_movies.feature.home.domain.usecase.getListTv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.data.ResultItem
import kotlinx.coroutines.flow.Flow

interface GetListTvUseCase {
  fun getPagingPopularTv(): Flow<PagingData<ResultItem>>
  fun getPagingOnTv(): Flow<PagingData<ResultItem>>
  fun getPagingAiringTodayTv(): Flow<PagingData<ResultItem>>
  fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>>
}
