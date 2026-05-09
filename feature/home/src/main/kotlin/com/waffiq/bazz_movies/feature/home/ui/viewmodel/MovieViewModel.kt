package com.waffiq.bazz_movies.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.data.domain.usecase.listmovie.GetListMoviesUseCase
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.home.ui.domain.TrendingPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val getListMoviesUseCase: GetListMoviesUseCase) :
  ViewModel() {

  private val _trendingPeriod = MutableStateFlow(TrendingPeriod.WEEK)
  val trendingPeriod: StateFlow<TrendingPeriod> = _trendingPeriod.asStateFlow()

  val trending: Flow<PagingData<MediaItem>> = _trendingPeriod
    .flatMapLatest { period ->
      when (period) {
        TrendingPeriod.TODAY -> getListMoviesUseCase.getTrendingToday()
        TrendingPeriod.WEEK -> getListMoviesUseCase.getTrendingThisWeek()
      }
    }
    .cachedIn(viewModelScope)

  fun setTrendingPeriod(period: TrendingPeriod) {
    _trendingPeriod.value = period
  }

  fun getTopRatedMovies(): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getTopRatedMovies().cachedIn(viewModelScope)

  fun getPopularMovies(): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getPopularMovies().cachedIn(viewModelScope)

  fun getUpcomingMovies(): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getUpcomingMovies().cachedIn(viewModelScope)

  fun getPlayingNowMovies(): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getPlayingNowMovies().cachedIn(viewModelScope)
}
