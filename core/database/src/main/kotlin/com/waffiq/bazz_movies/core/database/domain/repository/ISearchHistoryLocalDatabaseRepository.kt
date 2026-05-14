package com.waffiq.bazz_movies.core.database.domain.repository

import com.waffiq.bazz_movies.core.models.SearchHistory
import kotlinx.coroutines.flow.Flow

interface ISearchHistoryLocalDatabaseRepository {
  fun getSearchHistory(): Flow<List<SearchHistory>>
  suspend fun insert(item: SearchHistory)
  suspend fun deleteByQuery(query: String): Int
  suspend fun delete(item: SearchHistory): Int
  suspend fun deleteAll(): Int
  suspend fun trimHistory(): Int
}
