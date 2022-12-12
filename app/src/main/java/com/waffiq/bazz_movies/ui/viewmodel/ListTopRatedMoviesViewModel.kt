package com.waffiq.bazz_movies.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.model.Genre
import com.waffiq.bazz_movies.data.remote.ResultResponse
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import kotlinx.coroutines.launch

class ListTopRatedMoviesViewModel(
  private val movieRepository: MoviesRepository,
  ): ViewModel() {

  fun getTopRatedMovies() = movieRepository.getPagingTopRatedMovies().cachedIn(viewModelScope).asLiveData()

  fun getMoviesGenres() = movieRepository.movieGenres
}