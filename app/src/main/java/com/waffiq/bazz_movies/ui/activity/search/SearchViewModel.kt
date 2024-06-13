package com.waffiq.bazz_movies.ui.activity.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.domain.model.search.ResultsItemSearch

class SearchViewModel(
  private val movieRepository: MoviesRepository
) : ViewModel() {

  fun search(query: String): LiveData<PagingData<ResultsItemSearch>> =
    movieRepository.getPagingSearch(query).cachedIn(viewModelScope).asLiveData()
}