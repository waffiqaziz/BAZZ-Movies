package com.waffiq.bazz_movies.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.data.domain.usecase.asian.GetAsianMediaUseCase
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.feature.home.ui.domain.AnimePeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class AsianViewModel @Inject constructor(private val getAsianMediaUseCase: GetAsianMediaUseCase) :
  ViewModel() {

  private val _animePeriod = MutableStateFlow(AnimePeriod.THIS_SEASON)
  val animePeriod: StateFlow<AnimePeriod> = _animePeriod.asStateFlow()

  val anime: Flow<PagingData<MediaItem>> = _animePeriod
    .flatMapLatest { period ->
      when (period) {
        AnimePeriod.ALL_TIME -> getAsianMediaUseCase.getAnimeAllTime()
        AnimePeriod.THIS_SEASON -> getAsianMediaUseCase.getAnimeThisSeason()
      }
    }
    .cachedIn(viewModelScope)

  fun setAnimePeriod(period: AnimePeriod) {
    _animePeriod.value = period
  }

  fun getDonghua(): Flow<PagingData<MediaItem>> =
    getAsianMediaUseCase.getDonghua().cachedIn(viewModelScope)

  fun getAsianRomance(): Flow<PagingData<MediaItem>> =
    getAsianMediaUseCase.getAsianRomance().cachedIn(viewModelScope)

  fun getCostumeDrama(): Flow<PagingData<MediaItem>> =
    getAsianMediaUseCase.getCostumeDrama().cachedIn(viewModelScope)

  fun getRealityShow(): Flow<PagingData<MediaItem>> =
    getAsianMediaUseCase.getRealityShow().cachedIn(viewModelScope)
}
