package com.waffiq.bazz_movies.feature.home.domain.usecase.getListTv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListTvInteractor @Inject constructor(
  private val getListTvRepository: IMoviesRepository
) : GetListTvUseCase {
  override fun getPagingPopularTv(): Flow<PagingData<ResultItem>> =
    getListTvRepository.getPagingPopularTv()

  override fun getPagingOnTv(): Flow<PagingData<ResultItem>> =
    getListTvRepository.getPagingOnTv()

  override fun getPagingAiringTodayTv(): Flow<PagingData<ResultItem>> =
    getListTvRepository.getPagingAiringTodayTv()

  override fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>> =
    getListTvRepository.getPagingTopRatedTv()
}
