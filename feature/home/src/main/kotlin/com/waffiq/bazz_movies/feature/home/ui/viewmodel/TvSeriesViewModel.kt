package com.waffiq.bazz_movies.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.home.domain.usecase.getListTv.GetListTvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TvSeriesViewModel @Inject constructor(private val getListTvUseCase: GetListTvUseCase) :
  ViewModel() {
  fun getPopularTv(region: String): Flow<PagingData<MediaItem>> =
    getListTvUseCase.getPopularTv(region).cachedIn(viewModelScope)

  fun getAiringThisWeekTv(region: String): Flow<PagingData<MediaItem>> =
    getListTvUseCase.getAiringThisWeekTv(region).cachedIn(viewModelScope)

  fun getAiringTodayTv(region: String): Flow<PagingData<MediaItem>> =
    getListTvUseCase.getAiringTodayTv(region).cachedIn(viewModelScope)

  fun getTopRatedTv(): Flow<PagingData<MediaItem>> =
    getListTvUseCase.getTopRatedTv().cachedIn(viewModelScope)
}
