package com.waffiq.bazz_movies.ui.activity.home

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.repository.MoviesRepository

class HomeViewModel(
  private val movieRepository: MoviesRepository
) : ViewModel() {

  fun getTrendingWeek(region : String) = movieRepository.getPagingTrendingWeek(region).cachedIn(viewModelScope).asLiveData()

  fun getTrendingDay(region : String) = movieRepository.getPagingTrendingDay(region).cachedIn(viewModelScope).asLiveData()

  fun getUpcomingMovies(region : String) = movieRepository.getPagingUpcomingMovies(region).cachedIn(viewModelScope).asLiveData()

  fun getPlayingNowMovies(region : String) = movieRepository.getPagingPlayingNowMovies(region).cachedIn(viewModelScope).asLiveData()

  fun getTopRatedMovies() = movieRepository.getPagingTopRatedMovies().cachedIn(viewModelScope).asLiveData()

  fun getPopularMovies() = movieRepository.getPagingPopularMovies().cachedIn(viewModelScope).asLiveData()

  fun getPopularTv() = movieRepository.getPagingPopularTv().cachedIn(viewModelScope).asLiveData()

  fun getOnTv() = movieRepository.getPagingOnTv().cachedIn(viewModelScope).asLiveData()

  fun getAiringTodayTv() = movieRepository.getPagingAiringTodayTv().cachedIn(viewModelScope).asLiveData()

  fun getTopRatedTv() = movieRepository.getPagingTopRatedTv().cachedIn(viewModelScope).asLiveData()
}