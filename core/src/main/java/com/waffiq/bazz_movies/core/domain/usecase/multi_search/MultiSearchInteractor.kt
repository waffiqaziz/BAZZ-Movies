package com.waffiq.bazz_movies.core.domain.usecase.multi_search

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.model.search.ResultsItemSearch
import com.waffiq.bazz_movies.core.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MultiSearchInteractor @Inject constructor(
  private val multiSearchRespository: IMoviesRepository
) : MultiSearchUseCase {
  override fun search(query: String): Flow<PagingData<ResultsItemSearch>> =
    multiSearchRespository.getPagingSearch(query)
}
