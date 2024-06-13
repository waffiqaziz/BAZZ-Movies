package com.waffiq.bazz_movies.ui.activity.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.usecase.get_list_tv.GetListTvUseCase

class TvSeriesViewModel(
  private val getListTvUseCase: GetListTvUseCase
) : ViewModel() {
  fun getPopularTv(): LiveData<PagingData<ResultItem>> =
    getListTvUseCase.getPagingPopularTv().cachedIn(viewModelScope).asLiveData()

  fun getOnTv(): LiveData<PagingData<ResultItem>> =
    getListTvUseCase.getPagingOnTv().cachedIn(viewModelScope).asLiveData()

  fun getAiringTodayTv(): LiveData<PagingData<ResultItem>> =
    getListTvUseCase.getPagingAiringTodayTv().cachedIn(viewModelScope).asLiveData()

  fun getTopRatedTv(): LiveData<PagingData<ResultItem>> =
    getListTvUseCase.getPagingTopRatedTv().cachedIn(viewModelScope).asLiveData()
}