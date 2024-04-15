package com.waffiq.bazz_movies.ui.activity.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.repository.MoviesRepository

class SearchViewModel(
  private val movieRepository: MoviesRepository
) : ViewModel() {

  fun search(query: String) = movieRepository.getPagingSearch(query).cachedIn(viewModelScope).asLiveData()
}