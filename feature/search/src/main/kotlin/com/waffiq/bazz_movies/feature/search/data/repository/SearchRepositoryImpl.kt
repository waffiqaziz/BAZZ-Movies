package com.waffiq.bazz_movies.feature.search.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.common.MediaType
import com.waffiq.bazz_movies.core.network.data.remote.datasource.search.SearchRemoteDataSource
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.domain.repository.ISearchRepository
import com.waffiq.bazz_movies.feature.search.utils.SearchMapper.toMultiSearchItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
  private val searchDataSource: SearchRemoteDataSource,
) : ISearchRepository {

  override fun search(query: String, filters: Set<MediaType>): Flow<PagingData<MultiSearchItem>> =
    searchDataSource.search(query, filters).map { pagingData ->
      pagingData.map { responseItem ->
        val knownType =
          if (filters.size == 1) filters.first().takeIf { it != MediaType.MULTI } else null
        responseItem.toMultiSearchItem(knownType)
      }
    }
}
