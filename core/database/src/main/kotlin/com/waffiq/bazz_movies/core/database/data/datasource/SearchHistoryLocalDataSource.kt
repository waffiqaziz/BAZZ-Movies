package com.waffiq.bazz_movies.core.database.data.datasource

import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.database.data.model.SearchHistoryEntity
import com.waffiq.bazz_movies.core.database.data.room.SearchHistoryDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchHistoryLocalDataSource @Inject constructor(
  private val searchHistoryDao: SearchHistoryDao,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : SearchHistoryLocalDataSourceInterface {

  override fun getSearchHistory(): Flow<List<SearchHistoryEntity>> =
    searchHistoryDao.getSearchHistory().flowOn(ioDispatcher)

  override suspend fun insert(item: SearchHistoryEntity) = searchHistoryDao.insert(item)

  override suspend fun deleteByQuery(query: String): Int = searchHistoryDao.deleteByQuery(query)

  override suspend fun delete(item: SearchHistoryEntity): Int = searchHistoryDao.delete(item)

  override suspend fun deleteAll(): Int = searchHistoryDao.deleteAll()

  override suspend fun trimHistory(): Int = searchHistoryDao.trimHistory()
}
