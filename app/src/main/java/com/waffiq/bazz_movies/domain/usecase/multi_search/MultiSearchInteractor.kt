package com.waffiq.bazz_movies.domain.usecase.multi_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.search.ResultsItemSearch
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository

class MultiSearchInteractor(
  private val multiSearchRespository: IMoviesRepository
) : MultiSearchUseCase {
  override fun search(query: String): LiveData<PagingData<ResultsItemSearch>> =
    multiSearchRespository.getPagingSearch(query).asLiveData()
}