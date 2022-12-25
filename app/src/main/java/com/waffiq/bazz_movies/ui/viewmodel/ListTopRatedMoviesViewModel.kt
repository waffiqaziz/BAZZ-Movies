package com.waffiq.bazz_movies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.repository.MoviesRepository

class ListTopRatedMoviesViewModel(
  private val movieRepository: MoviesRepository,
  ): ViewModel() {

  fun getTopRatedMovies() = movieRepository.getPagingTopRatedMovies().cachedIn(viewModelScope).asLiveData()
}