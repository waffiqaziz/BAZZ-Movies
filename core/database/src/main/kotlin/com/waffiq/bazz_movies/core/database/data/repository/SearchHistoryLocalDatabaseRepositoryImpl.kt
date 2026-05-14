package com.waffiq.bazz_movies.core.database.data.repository

import com.waffiq.bazz_movies.core.database.data.datasource.SearchHistoryLocalDataSource
import com.waffiq.bazz_movies.core.database.data.model.SearchHistoryEntity
import com.waffiq.bazz_movies.core.database.domain.repository.ISearchHistoryLocalDatabaseRepository
import com.waffiq.bazz_movies.core.database.utils.SearchHistoryMapper.toSearchHistory
import com.waffiq.bazz_movies.core.database.utils.SearchHistoryMapper.toSearchHistoryEntity
import com.waffiq.bazz_movies.core.models.SearchHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchHistoryLocalDatabaseRepositoryImpl @Inject constructor(
  private val searchHistoryLocalDataSource: SearchHistoryLocalDataSource,
) : ISearchHistoryLocalDatabaseRepository {

  override fun getSearchHistory(): Flow<List<SearchHistory>> =
    searchHistoryLocalDataSource.getSearchHistory().map { list ->
      list.map { it.toSearchHistory() }
    }

  override suspend fun insert(item: SearchHistory) {
    searchHistoryLocalDataSource.insert(SearchHistoryEntity(query = item.query))
  }

  override suspend fun deleteByQuery(query: String): Int =
    searchHistoryLocalDataSource.deleteByQuery(query)

  override suspend fun delete(item: SearchHistory): Int =
    searchHistoryLocalDataSource.delete(item.toSearchHistoryEntity())

  override suspend fun deleteAll(): Int = searchHistoryLocalDataSource.deleteAll()

  override suspend fun trimHistory(): Int = searchHistoryLocalDataSource.trimHistory()
}
