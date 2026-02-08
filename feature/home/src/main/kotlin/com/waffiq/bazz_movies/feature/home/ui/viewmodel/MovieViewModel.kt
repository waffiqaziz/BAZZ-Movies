package com.waffiq.bazz_movies.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.home.domain.usecase.getListMovie.GetListMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val getListMoviesUseCase: GetListMoviesUseCase) :
  ViewModel() {
  fun getTopRatedMovies(): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getTopRatedMovies().cachedIn(viewModelScope)

  fun getPopularMovies(): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getPopularMovies().cachedIn(viewModelScope)

  fun getTrendingWeek(region: String): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getTrendingThisWeek(region).cachedIn(viewModelScope)

  fun getTrendingDay(region: String): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getTrendingToday(region).cachedIn(viewModelScope)

  fun getUpcomingMovies(region: String): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getUpcomingMovies(region).cachedIn(viewModelScope)

  fun getPlayingNowMovies(region: String): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getPlayingNowMovies(region).cachedIn(viewModelScope)
}
