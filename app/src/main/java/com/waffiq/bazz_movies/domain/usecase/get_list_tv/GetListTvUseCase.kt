package com.waffiq.bazz_movies.domain.usecase.get_list_tv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.ResultItem
import kotlinx.coroutines.flow.Flow

interface GetListTvUseCase {
  fun getPagingPopularTv(): Flow<PagingData<ResultItem>>
  fun getPagingOnTv(): Flow<PagingData<ResultItem>>
  fun getPagingAiringTodayTv(): Flow<PagingData<ResultItem>>
  fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>>
}