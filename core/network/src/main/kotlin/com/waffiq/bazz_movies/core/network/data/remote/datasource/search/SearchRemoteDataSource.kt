package com.waffiq.bazz_movies.core.network.data.remote.datasource.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.network.data.remote.datasource.movie.MovieRemoteDataSource.Companion.PAGE_SIZE
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.SearchPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.SearchApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRemoteDataSource @Inject constructor(
  private val searchApiService: SearchApiService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : SearchRemoteDataSourceInterface {

  override fun search(query: String): Flow<PagingData<MultiSearchResponseItem>> =
    Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { SearchPagingSource(searchApiService, query) },
    ).flow.flowOn(ioDispatcher)
}
