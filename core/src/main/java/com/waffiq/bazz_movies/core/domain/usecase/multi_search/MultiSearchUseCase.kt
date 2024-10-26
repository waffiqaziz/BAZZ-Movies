package com.waffiq.bazz_movies.core.domain.usecase.multi_search

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.model.search.ResultsItemSearch
import kotlinx.coroutines.flow.Flow

interface MultiSearchUseCase {
  fun search(query: String): Flow<PagingData<ResultsItemSearch>>
}
