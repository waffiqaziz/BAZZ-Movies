package com.waffiq.bazz_movies.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.data.ResultItem
import com.waffiq.bazz_movies.feature.home.domain.usecase.getListTv.GetListTvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TvSeriesViewModel @Inject constructor(
  private val getListTvUseCase: GetListTvUseCase
) : ViewModel() {
  fun getPopularTv(region: String): Flow<PagingData<ResultItem>> =
    getListTvUseCase.getPagingPopularTv(region).cachedIn(viewModelScope)

  fun getOnTv(): Flow<PagingData<ResultItem>> =
    getListTvUseCase.getPagingOnTv().cachedIn(viewModelScope)

  fun getAiringTodayTv(region: String): Flow<PagingData<ResultItem>> =
    getListTvUseCase.getPagingAiringTodayTv(region).cachedIn(viewModelScope)

  fun getTopRatedTv(): Flow<PagingData<ResultItem>> =
    getListTvUseCase.getPagingTopRatedTv().cachedIn(viewModelScope)
}
