package com.waffiq.bazz_movies.feature.search.domain.usecase

import androidx.paging.PagingData
import com.waffiq.bazz_movies.feature.search.domain.model.ResultsItemSearch
import kotlinx.coroutines.flow.Flow

interface MultiSearchUseCase {
  fun search(query: String): Flow<PagingData<ResultsItemSearch>>
}
