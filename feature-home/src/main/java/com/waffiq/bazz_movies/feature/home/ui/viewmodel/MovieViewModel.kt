package com.waffiq.bazz_movies.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.feature.home.domain.usecase.getListMovie.GetListMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
  private val getListMoviesUseCase: GetListMoviesUseCase
) : ViewModel() {
  fun getTopRatedMovies(): Flow<PagingData<ResultItem>> =
    getListMoviesUseCase.getPagingTopRatedMovies().cachedIn(viewModelScope)

  fun getPopularMovies(): Flow<PagingData<ResultItem>> =
    getListMoviesUseCase.getPagingPopularMovies().cachedIn(viewModelScope)

  fun getTrendingWeek(region: String): Flow<PagingData<ResultItem>> =
    getListMoviesUseCase.getPagingTrendingWeek(region).cachedIn(viewModelScope)

  fun getTrendingDay(region: String): Flow<PagingData<ResultItem>> =
    getListMoviesUseCase.getPagingTrendingDay(region).cachedIn(viewModelScope)

  fun getUpcomingMovies(region: String): Flow<PagingData<ResultItem>> =
    getListMoviesUseCase.getPagingUpcomingMovies(region).cachedIn(viewModelScope)

  fun getPlayingNowMovies(region: String): Flow<PagingData<ResultItem>> =
    getListMoviesUseCase.getPagingPlayingNowMovies(region).cachedIn(viewModelScope)
}
