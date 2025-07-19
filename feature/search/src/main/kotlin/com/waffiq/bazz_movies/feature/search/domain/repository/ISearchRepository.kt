package com.waffiq.bazz_movies.feature.search.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import kotlinx.coroutines.flow.Flow

interface ISearchRepository {
  fun search(query: String): Flow<PagingData<MultiSearchItem>>
}
