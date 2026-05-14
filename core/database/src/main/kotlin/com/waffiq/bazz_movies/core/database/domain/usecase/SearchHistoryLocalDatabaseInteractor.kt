package com.waffiq.bazz_movies.core.database.domain.usecase

import com.waffiq.bazz_movies.core.database.domain.repository.ISearchHistoryLocalDatabaseRepository
import com.waffiq.bazz_movies.core.models.SearchHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchHistoryLocalDatabaseInteractor @Inject constructor(
  private val searchHistoryDatabaseRepository: ISearchHistoryLocalDatabaseRepository,
) : SearchHistoryLocalDatabaseUseCase {

  override fun getSearchHistory(): Flow<List<SearchHistory>> =
    searchHistoryDatabaseRepository.getSearchHistory()

  override suspend fun insert(item: SearchHistory) = searchHistoryDatabaseRepository.insert(item)

  override suspend fun deleteByQuery(query: String): Int =
    searchHistoryDatabaseRepository.deleteByQuery(query)

  override suspend fun delete(item: SearchHistory): Int =
    searchHistoryDatabaseRepository.delete(item)

  override suspend fun deleteAll(): Int = searchHistoryDatabaseRepository.deleteAll()

  override suspend fun trimHistory(): Int = searchHistoryDatabaseRepository.trimHistory()
}
