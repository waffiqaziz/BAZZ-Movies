package com.waffiq.bazz_movies.domain.usecase.multi_search

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.search.ResultsItemSearch
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow

class MultiSearchInteractor(
  private val multiSearchRespository: IMoviesRepository
) : MultiSearchUseCase {
  override fun search(query: String): Flow<PagingData<ResultsItemSearch>> =
    multiSearchRespository.getPagingSearch(query)
}
