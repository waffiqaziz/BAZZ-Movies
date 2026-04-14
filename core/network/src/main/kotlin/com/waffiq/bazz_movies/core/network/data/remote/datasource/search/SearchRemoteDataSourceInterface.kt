package com.waffiq.bazz_movies.core.network.data.remote.datasource.search

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponseItem
import kotlinx.coroutines.flow.Flow

fun interface SearchRemoteDataSourceInterface {
  fun search(query: String): Flow<PagingData<MultiSearchResponseItem>>
}
