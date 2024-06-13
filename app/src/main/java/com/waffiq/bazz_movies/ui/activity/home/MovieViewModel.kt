package com.waffiq.bazz_movies.ui.activity.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.usecase.get_list_movies.GetListMoviesUseCase

class MovieViewModel(
  private val getListMoviesUseCase: GetListMoviesUseCase
) : ViewModel() {
  fun getTopRatedMovies(): LiveData<PagingData<ResultItem>> =
    getListMoviesUseCase.getPagingTopRatedMovies().cachedIn(viewModelScope).asLiveData()

  fun getPopularMovies(): LiveData<PagingData<ResultItem>> =
    getListMoviesUseCase.getPagingPopularMovies().cachedIn(viewModelScope).asLiveData()

  fun getTrendingWeek(region: String): LiveData<PagingData<ResultItem>> =
    getListMoviesUseCase.getPagingTrendingWeek(region).cachedIn(viewModelScope).asLiveData()

  fun getTrendingDay(region: String): LiveData<PagingData<ResultItem>> =
    getListMoviesUseCase.getPagingTrendingDay(region).cachedIn(viewModelScope).asLiveData()

  fun getUpcomingMovies(region: String): LiveData<PagingData<ResultItem>> =
    getListMoviesUseCase.getPagingUpcomingMovies(region).cachedIn(viewModelScope).asLiveData()

  fun getPlayingNowMovies(region: String): LiveData<PagingData<ResultItem>> =
    getListMoviesUseCase.getPagingPlayingNowMovies(region).cachedIn(viewModelScope).asLiveData()
}