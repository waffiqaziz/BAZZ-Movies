package com.waffiq.bazz_movies.domain.usecase.multi_search

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.search.ResultsItemSearch

interface MultiSearchUseCase {
  fun search(query: String): LiveData<PagingData<ResultsItemSearch>>
}