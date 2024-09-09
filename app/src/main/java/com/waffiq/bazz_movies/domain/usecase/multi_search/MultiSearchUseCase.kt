package com.waffiq.bazz_movies.domain.usecase.multi_search

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.search.ResultsItemSearch
import kotlinx.coroutines.flow.Flow

interface MultiSearchUseCase {
  fun search(query: String): Flow<PagingData<ResultsItemSearch>>
}