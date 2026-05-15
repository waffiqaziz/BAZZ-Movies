package com.waffiq.bazz_movies.core.database.data.datasource

import com.waffiq.bazz_movies.core.database.data.model.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

interface SearchHistoryLocalDataSourceInterface {
  fun getSearchHistory(): Flow<List<SearchHistoryEntity>>
  suspend fun insert(item: SearchHistoryEntity)
  suspend fun deleteByQuery(query: String): Int
  suspend fun delete(item: SearchHistoryEntity): Int
  suspend fun deleteAll(): Int
  suspend fun trimHistory(): Int
}
