package com.waffiq.bazz_movies.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.movie.domain.usecase.listtv.GetListTvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TvSeriesViewModel @Inject constructor(private val getListTvUseCase: GetListTvUseCase) :
  ViewModel() {
  fun getPopularTv(): Flow<PagingData<MediaItem>> =
    getListTvUseCase.getPopularTv().cachedIn(viewModelScope)

  fun getAiringThisWeekTv(): Flow<PagingData<MediaItem>> =
    getListTvUseCase.getAiringThisWeekTv().cachedIn(viewModelScope)

  fun getAiringTodayTv(): Flow<PagingData<MediaItem>> =
    getListTvUseCase.getAiringTodayTv().cachedIn(viewModelScope)

  fun getTopRatedTv(): Flow<PagingData<MediaItem>> =
    getListTvUseCase.getTopRatedTv().cachedIn(viewModelScope)
}
