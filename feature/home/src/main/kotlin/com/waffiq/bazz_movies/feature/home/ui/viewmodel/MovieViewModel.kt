package com.waffiq.bazz_movies.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.movie.domain.usecase.listmovie.GetListMoviesUseCase
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

  fun getTrendingWeek(): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getTrendingThisWeek().cachedIn(viewModelScope)

  fun getTrendingDay(): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getTrendingToday().cachedIn(viewModelScope)

  fun getUpcomingMovies(): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getUpcomingMovies().cachedIn(viewModelScope)

  fun getPlayingNowMovies(): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getPlayingNowMovies().cachedIn(viewModelScope)
}
