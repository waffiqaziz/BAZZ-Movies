package com.waffiq.bazz_movies.feature.search.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.model.search.ResultsItemSearch
import kotlinx.coroutines.flow.Flow

interface ISearchRepository {

  fun getPagingSearch(query: String): Flow<PagingData<ResultsItemSearch>>
}
