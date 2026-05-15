package com.waffiq.bazz_movies.core.database.utils

import com.waffiq.bazz_movies.core.database.testdummy.DummyData.searchHistory
import com.waffiq.bazz_movies.core.database.testdummy.DummyData.searchHistoryEntity
import com.waffiq.bazz_movies.core.database.utils.SearchHistoryMapper.toSearchHistory
import com.waffiq.bazz_movies.core.database.utils.SearchHistoryMapper.toSearchHistoryEntity
import org.junit.Test
import kotlin.test.assertEquals

class SearchHistoryMapperTest {

  @Test
  fun toSearchHistoryEntity_withValidAllTrueValues_returnsSearchHistoryEntity() {
    assertEquals(searchHistory.toSearchHistoryEntity().query, searchHistory.query)
  }

  @Test
  fun toSearchHistory_withValidAllTrueValues_returnsSearchHistory() {
    assertEquals(searchHistoryEntity.toSearchHistory().createdAt, searchHistoryEntity.createdAt)
  }
}
