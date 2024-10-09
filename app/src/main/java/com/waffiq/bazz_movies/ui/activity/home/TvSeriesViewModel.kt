package com.waffiq.bazz_movies.ui.activity.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.usecase.get_list_tv.GetListTvUseCase
import kotlinx.coroutines.flow.Flow

class TvSeriesViewModel (
  private val getListTvUseCase: GetListTvUseCase
) : ViewModel() {
  fun getPopularTv(): Flow<PagingData<ResultItem>> =
    getListTvUseCase.getPagingPopularTv().cachedIn(viewModelScope)

  fun getOnTv(): Flow<PagingData<ResultItem>> =
    getListTvUseCase.getPagingOnTv().cachedIn(viewModelScope)

  fun getAiringTodayTv(): Flow<PagingData<ResultItem>> =
    getListTvUseCase.getPagingAiringTodayTv().cachedIn(viewModelScope)

  fun getTopRatedTv(): Flow<PagingData<ResultItem>> =
    getListTvUseCase.getPagingTopRatedTv().cachedIn(viewModelScope)
}
