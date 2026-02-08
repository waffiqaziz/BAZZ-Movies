package com.waffiq.bazz_movies.feature.search.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.domain.repository.ISearchRepository
import com.waffiq.bazz_movies.feature.search.utils.SearchMapper.toMultiSearchItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(private val movieDataSource: MovieDataSource) :
  ISearchRepository {

  override fun search(query: String): Flow<PagingData<MultiSearchItem>> =
    movieDataSource.search(query).map { pagingData ->
      pagingData.map { it.toMultiSearchItem() }
    }
}
