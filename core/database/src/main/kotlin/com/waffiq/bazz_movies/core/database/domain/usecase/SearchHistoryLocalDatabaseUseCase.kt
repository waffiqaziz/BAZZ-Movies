package com.waffiq.bazz_movies.core.database.domain.usecase

import com.waffiq.bazz_movies.core.model.SearchHistory
import kotlinx.coroutines.flow.Flow

interface SearchHistoryLocalDatabaseUseCase {
  fun getSearchHistory(): Flow<List<SearchHistory>>
  suspend fun insert(item: SearchHistory)
  suspend fun deleteByQuery(query: String): Int
  suspend fun delete(item: SearchHistory): Int
  suspend fun deleteAll(): Int
  suspend fun trimHistory(): Int
}
